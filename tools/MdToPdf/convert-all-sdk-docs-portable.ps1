[CmdletBinding()]
param(
    [Alias("i")]
    [string] $InputPath,

    [Alias("o")]
    [string] $OutputPath,

    [string] $TempPath,

    [switch] $NoPreprocess,

    [switch] $NoClean
)

$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$RepoRoot = Resolve-Path (Join-Path $ScriptDir "..\..")

if (-not $InputPath) {
    $InputPath = Join-Path $RepoRoot "docs"
}

if (-not $OutputPath) {
    $OutputPath = Join-Path $RepoRoot "build\release\manuals"
}

if (-not $TempPath) {
    $TempPath = Join-Path $RepoRoot "build\md-to-pdf"
}

$NodeArgs = @(
    (Join-Path $ScriptDir "convert-md-to-pdf.js"),
    "--input", $InputPath,
    "--output", $OutputPath,
    "--repo-root", $RepoRoot,
    "--temp", $TempPath
)

if (-not $NoClean) {
    $NodeArgs += "--clean"
}

if ($NoPreprocess) {
    $NodeArgs += "--no-preprocess"
}

& node @NodeArgs
exit $LASTEXITCODE
