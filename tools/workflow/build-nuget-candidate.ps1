param([Parameter(Mandatory)][string]$Version)
$ErrorActionPreference = 'Stop'
if ($Version -notmatch '^\d+\.\d+\.\d+(-[0-9A-Za-z.-]+)?$') { throw 'Invalid package version' }
function Invoke-Checked([string]$Command, [string[]]$Arguments) {
    & $Command @Arguments
    if ($LASTEXITCODE -ne 0) { throw "$Command failed: $LASTEXITCODE" }
}
$repo = (Resolve-Path (Join-Path $PSScriptRoot '../..')).Path
$generatedVersion = Join-Path $repo 'dotnet/M3Mobile.M3Sdk.Xamarin/Properties/GeneratedVersionInfo.cs'
$originalVersion = if (Test-Path -LiteralPath $generatedVersion) { [System.IO.File]::ReadAllBytes($generatedVersion) } else { $null }
Push-Location $repo
try {
    Invoke-Checked python @('tools/workflow/check-xamarin-sources.py')
    Invoke-Checked dotnet @('test', 'dotnet/M3Mobile.M3Sdk.Xamarin.Tests/M3Mobile.M3Sdk.Xamarin.Tests.csproj', '-p:RestoreSources=https://api.nuget.org/v3/index.json', '--configuration', 'Release', '--logger', 'trx', '--results-directory', 'build/workflow-tests')
    Invoke-Checked node @('tools/release/write-xamarin-version.js', '--version', $Version, '--repo-root', '.')
    $sdk = @($env:ANDROID_HOME, $env:ANDROID_SDK_ROOT, "$env:LOCALAPPDATA/Android/Sdk") |
        Where-Object { $_ -and (Test-Path -LiteralPath $_) } | Select-Object -First 1
    if (-not $sdk) { throw 'Android SDK required' }
    if (-not (Test-Path -LiteralPath "$sdk/platforms/android-24/android.jar")) {
        $manager = Get-ChildItem -LiteralPath "$sdk/cmdline-tools" -Filter sdkmanager.bat -Recurse | Select-Object -First 1
        if (-not $manager) { throw 'sdkmanager required to install Android API 24' }
        Invoke-Checked $manager.FullName @("--sdk_root=$sdk", 'platforms;android-24')
    }
    # Keep classic Xamarin intermediates separate from SDK-style .NET project assets.
    $intermediate = "$repo/dotnet/M3Mobile.M3Sdk.Xamarin/obj/workflow-xamarin/"
    Invoke-Checked msbuild @('dotnet/M3Mobile.M3Sdk.Xamarin/M3Mobile.M3Sdk.Xamarin.csproj', '/t:Build', '/p:Configuration=Release', '/p:Platform=AnyCPU', "/p:AndroidSdkDirectory=$sdk", "/p:BaseIntermediateOutputPath=$intermediate", "/p:MSBuildProjectExtensionsPath=$intermediate")
    New-Item -ItemType Directory -Force build/candidate/nuget | Out-Null
    $stageRoot = "$repo/build/nuget-source"
    $packageRoot = "$stageRoot/dotnet/M3Mobile.M3Sdk.Xamarin"
    New-Item -ItemType Directory -Force "$packageRoot/bin/Release" | Out-Null
    foreach ($item in @('README.md', 'M3Mobile.M3Sdk.Xamarin.nuspec', 'build', 'buildTransitive', 'manifest')) {
        Copy-Item -LiteralPath "dotnet/M3Mobile.M3Sdk.Xamarin/$item" -Destination $packageRoot -Recurse -Force
    }
    foreach ($extension in @('dll', 'xml')) {
        Copy-Item -LiteralPath "dotnet/M3Mobile.M3Sdk.Xamarin/bin/Release/M3Mobile.M3Sdk.Xamarin.$extension" -Destination "$packageRoot/bin/Release" -Force
    }
    Invoke-Checked node @('tools/release/prepare-release.js', '--version', $Version, '--repo-root', $stageRoot)
    Invoke-Checked nuget @('pack', "$packageRoot/M3Mobile.M3Sdk.Xamarin.nuspec", '-BasePath', $packageRoot, '-Version', $Version, '-Properties', "version=$Version;Configuration=Release", '-OutputDirectory', 'build/candidate/nuget')
    Invoke-Checked dotnet @('workload', 'install', 'android', '--source', 'https://api.nuget.org/v3/index.json')
    $feed = [System.Security.SecurityElement]::Escape("$repo/build/candidate/nuget")
    # Mapping pins M3Mobile.M3Sdk.Xamarin to this exact candidate feed, never nuget.org.
    @"
<configuration><packageSources><clear/><add key="candidate" value="$feed"/><add key="nuget" value="https://api.nuget.org/v3/index.json"/></packageSources><packageSourceMapping><packageSource key="candidate"><package pattern="M3Mobile.M3Sdk.Xamarin"/></packageSource><packageSource key="nuget"><package pattern="*"/></packageSource></packageSourceMapping></configuration>
"@ | Set-Content -Encoding utf8 build/candidate-feed.config
    $project = 'samples/dotnet-android-nuget/M3SdkPublishedSample.csproj'
    $platformVersion = & dotnet msbuild $project -getProperty:TargetPlatformVersion "-p:M3SdkVersion=$Version" -p:UseLocalM3Sdk=false
    if ($LASTEXITCODE -ne 0) { throw 'Could not determine consumer Android platform' }
    $api = (($platformVersion -join '').Trim() -split '\.')[0]
    if ($api -notmatch '^\d+$') { throw 'Invalid consumer Android platform' }
    if (-not (Test-Path -LiteralPath "$sdk/platforms/android-$api/android.jar")) {
        $manager = Get-ChildItem -LiteralPath "$sdk/cmdline-tools" -Filter sdkmanager.bat -Recurse | Select-Object -First 1
        if (-not $manager) { throw "sdkmanager required to install Android API $api" }
        Invoke-Checked $manager.FullName @("--sdk_root=$sdk", "platforms;android-$api")
    }
    $references = & dotnet msbuild $project -getItem:ProjectReference "-p:M3SdkVersion=$Version" -p:UseLocalM3Sdk=false
    if ($LASTEXITCODE -ne 0) { throw 'Could not inspect consumer dependencies' }
    if (@(($references -join "`n" | ConvertFrom-Json).Items.ProjectReference).Count -ne 0) {
        throw 'Package consumer must not reference a local SDK project'
    }
    # --no-cache does not bypass NuGet's global-packages folder. A new folder is
    # required when a developer rebuilds the same candidate version locally.
    $packages = "$repo/build/candidate-nuget-cache-$([Guid]::NewGuid().ToString('N'))"
    # Use the selected Android SDK for both classic Xamarin and the modern consumer.
    Invoke-Checked dotnet @('restore', $project, "-p:M3SdkVersion=$Version", '-p:UseLocalM3Sdk=false', "-p:AndroidSdkDirectory=$sdk", '--configfile', 'build/candidate-feed.config', '--packages', $packages, '--no-cache')
    Invoke-Checked dotnet @('build', $project, "-p:M3SdkVersion=$Version", '-p:UseLocalM3Sdk=false', "-p:AndroidSdkDirectory=$sdk", "-p:RestorePackagesPath=$packages", '--no-restore', '--configuration', 'Release')
    New-Item -ItemType Directory -Force build/candidate/dotnet-samples | Out-Null
    $apks = @(Get-ChildItem samples/dotnet-android-nuget/bin/Release -Filter *.apk -Recurse)
    if ($apks.Count -eq 0) { throw 'Package consumer produced no APK' }
    $apks | Copy-Item -Destination build/candidate/dotnet-samples
} finally {
    if ($null -ne $originalVersion) { [System.IO.File]::WriteAllBytes($generatedVersion, $originalVersion) }
    elseif (Test-Path -LiteralPath $generatedVersion) { Remove-Item -LiteralPath $generatedVersion }
    Pop-Location
}
