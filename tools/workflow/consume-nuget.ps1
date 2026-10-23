param(
    [Parameter(Mandatory)][string]$Version,
    [Parameter(Mandatory)][string]$Source,
    [Parameter(Mandatory)][string]$Feed
)
$ErrorActionPreference = 'Stop'
function Invoke-Checked([string]$Command, [string[]]$Arguments) {
    & $Command @Arguments
    if ($LASTEXITCODE -ne 0) { throw "$Command failed: $LASTEXITCODE" }
}
$sourceRoot = (Resolve-Path -LiteralPath $Source).Path
$feedRoot = (Resolve-Path -LiteralPath $Feed).Path
$escaped = [System.Security.SecurityElement]::Escape($feedRoot)
$config = Join-Path $env:RUNNER_TEMP 'rehearsal-nuget.config'
@"
<configuration><packageSources><clear/><add key="candidate" value="$escaped"/><add key="nuget" value="https://api.nuget.org/v3/index.json"/></packageSources><packageSourceMapping><packageSource key="candidate"><package pattern="M3Mobile.M3Sdk.Xamarin"/></packageSource><packageSource key="nuget"><package pattern="*"/></packageSource></packageSourceMapping></configuration>
"@ | Set-Content -Encoding utf8 $config
$packages = Join-Path $env:RUNNER_TEMP "rehearsal-cache-$([Guid]::NewGuid().ToString('N'))"
Push-Location $sourceRoot
try {
    Invoke-Checked dotnet @('workload', 'install', 'android', '--source', 'https://api.nuget.org/v3/index.json')
    $project = 'samples/dotnet-android-nuget/M3SdkPublishedSample.csproj'
    $refs = & dotnet msbuild $project -getItem:ProjectReference "-p:M3SdkVersion=$Version" -p:UseLocalM3Sdk=false
    if ($LASTEXITCODE -ne 0) { throw 'Cannot inspect consumer references' }
    if (@(($refs -join "`n" | ConvertFrom-Json).Items.ProjectReference).Count -ne 0) { throw 'Local SDK reference forbidden' }
    Invoke-Checked dotnet @('restore', $project, "-p:M3SdkVersion=$Version", '-p:UseLocalM3Sdk=false', "-p:AndroidSdkDirectory=$env:ANDROID_HOME", '--configfile', $config, '--packages', $packages, '--no-cache')
    Invoke-Checked dotnet @('build', $project, '-t:InstallAndroidDependencies', '--no-restore', '-p:AcceptAndroidSdkLicenses=true', "-p:M3SdkVersion=$Version", '-p:UseLocalM3Sdk=false', "-p:AndroidSdkDirectory=$env:ANDROID_HOME", "-p:RestorePackagesPath=$packages")
    Invoke-Checked dotnet @('build', $project, '--no-restore', '--configuration', 'Release', "-p:M3SdkVersion=$Version", '-p:UseLocalM3Sdk=false', "-p:AndroidSdkDirectory=$env:ANDROID_HOME", "-p:RestorePackagesPath=$packages")
} finally {
    Pop-Location
}
