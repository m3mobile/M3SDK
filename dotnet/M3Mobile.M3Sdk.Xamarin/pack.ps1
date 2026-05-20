param(
    [Parameter(Mandatory = $true)][string]$ProjectDir,
    [Parameter(Mandatory = $true)][string]$Configuration,
    [Parameter(Mandatory = $true)][string]$TargetPath,
    [Parameter(Mandatory = $true)][string]$DocumentationFile,
    [Parameter(Mandatory = $true)][string]$PackageOutputPath,
    [Parameter(Mandatory = $true)][string]$PackageId,
    [Parameter(Mandatory = $true)][string]$Version
)

$ErrorActionPreference = 'Stop'

$monoAndroidTfm = 'MonoAndroid7.0'
$packageRoot = Join-Path $ProjectDir "obj\nuget\$Configuration\package"
$libRoot = Join-Path $packageRoot "lib\$monoAndroidTfm"
$buildRoot = Join-Path $packageRoot 'build'
$buildTransitiveRoot = Join-Path $packageRoot 'buildTransitive'
$manifestRoot = Join-Path $packageRoot 'manifest'
$relsRoot = Join-Path $packageRoot '_rels'
$coreRoot = Join-Path $packageRoot 'package\services\metadata\core-properties'
$nuspecPath = Join-Path $ProjectDir "$PackageId.nuspec"
$readmePath = Join-Path $ProjectDir 'README.md'
$buildTargetsPath = Join-Path $ProjectDir "build\$PackageId.targets"
$buildTransitiveTargetsPath = Join-Path $ProjectDir "buildTransitive\$PackageId.targets"
$manifestPath = Join-Path $ProjectDir 'manifest\AndroidManifest.xml'

if (-not [System.IO.Path]::IsPathRooted($TargetPath)) {
    $TargetPath = Join-Path $ProjectDir $TargetPath
}
if (-not [System.IO.Path]::IsPathRooted($DocumentationFile)) {
    $DocumentationFile = Join-Path $ProjectDir $DocumentationFile
}
if (-not [System.IO.Path]::IsPathRooted($PackageOutputPath)) {
    $PackageOutputPath = Join-Path $ProjectDir $PackageOutputPath
}
$PackageOutputPath = [System.IO.Path]::GetFullPath($PackageOutputPath)

$packageFile = Join-Path $PackageOutputPath "$PackageId.$Version.nupkg"
$zipFile = Join-Path $PackageOutputPath "$PackageId.$Version.zip"
$coreFileName = ([guid]::NewGuid().ToString('N') + '.psmdcp')

if (-not (Test-Path -LiteralPath $TargetPath)) {
    throw "Target assembly was not found: $TargetPath"
}

if (-not (Test-Path -LiteralPath $nuspecPath)) {
    throw "Nuspec was not found: $nuspecPath"
}

if (-not (Test-Path -LiteralPath $buildTargetsPath)) {
    throw "NuGet build targets were not found: $buildTargetsPath"
}

if (-not (Test-Path -LiteralPath $buildTransitiveTargetsPath)) {
    throw "NuGet buildTransitive targets were not found: $buildTransitiveTargetsPath"
}

if (-not (Test-Path -LiteralPath $manifestPath)) {
    throw "NuGet Android manifest was not found: $manifestPath"
}

if (Test-Path -LiteralPath $packageRoot) {
    Remove-Item -LiteralPath $packageRoot -Recurse -Force
}

New-Item -ItemType Directory -Force -Path $libRoot, $buildRoot, $buildTransitiveRoot, $manifestRoot, $relsRoot, $coreRoot, $PackageOutputPath | Out-Null

Copy-Item -LiteralPath $TargetPath -Destination $libRoot -Force
if (Test-Path -LiteralPath $DocumentationFile) {
    Copy-Item -LiteralPath $DocumentationFile -Destination $libRoot -Force
}
Copy-Item -LiteralPath $buildTargetsPath -Destination $buildRoot -Force
Copy-Item -LiteralPath $buildTransitiveTargetsPath -Destination $buildTransitiveRoot -Force
Copy-Item -LiteralPath $manifestPath -Destination $manifestRoot -Force
Copy-Item -LiteralPath $nuspecPath -Destination $packageRoot -Force
Copy-Item -LiteralPath $readmePath -Destination $packageRoot -Force

@'
<?xml version="1.0" encoding="utf-8"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
  <Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml" />
  <Default Extension="psmdcp" ContentType="application/vnd.openxmlformats-package.core-properties+xml" />
  <Default Extension="dll" ContentType="application/octet" />
  <Default Extension="xml" ContentType="application/xml" />
  <Default Extension="md" ContentType="text/markdown" />
  <Default Extension="targets" ContentType="application/xml" />
  <Override PartName="/M3Mobile.M3Sdk.Xamarin.nuspec" ContentType="application/octet" />
</Types>
'@ | Set-Content -LiteralPath (Join-Path $packageRoot '[Content_Types].xml') -Encoding UTF8

@"
<?xml version="1.0" encoding="utf-8"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
  <Relationship Type="http://schemas.microsoft.com/packaging/2010/07/manifest" Target="/$PackageId.nuspec" Id="Rmanifest" />
  <Relationship Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="/package/services/metadata/core-properties/$coreFileName" Id="Rmetadata" />
</Relationships>
"@ | Set-Content -LiteralPath (Join-Path $relsRoot '.rels') -Encoding UTF8

@"
<?xml version="1.0" encoding="utf-8"?>
<coreProperties xmlns="http://schemas.openxmlformats.org/package/2006/metadata/core-properties"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:dcterms="http://purl.org/dc/terms/"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <dc:creator>M3 Mobile</dc:creator>
  <dc:description>Native C# Xamarin.Android SDK wrapper for M3 Mobile StartUp, ScanEmul, Time, Wifi, and Usb APIs.</dc:description>
  <dc:identifier>$PackageId</dc:identifier>
  <version>$Version</version>
</coreProperties>
"@ | Set-Content -LiteralPath (Join-Path $coreRoot $coreFileName) -Encoding UTF8

if (Test-Path -LiteralPath $packageFile) {
    Remove-Item -LiteralPath $packageFile -Force
}
if (Test-Path -LiteralPath $zipFile) {
    Remove-Item -LiteralPath $zipFile -Force
}

Compress-Archive -Path (Join-Path $packageRoot '*') -DestinationPath $zipFile -Force
Move-Item -LiteralPath $zipFile -Destination $packageFile -Force
Write-Host "Created package: $packageFile"
