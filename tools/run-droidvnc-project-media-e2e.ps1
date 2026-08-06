param(
    [string]$Serial = "2612031",
    [ValidateRange(1, 1000)]
    [int]$Runs = 10,
    [string]$ReportPath = ""
)

$ErrorActionPreference = "Stop"

$TargetPackage = "net.christianbeier.droidvnc_ng"
$StartUpPackage = "com.m3.startup"
$ClientPackage = "net.m3mobile.m3sdk"
$TestPackage = "net.m3mobile.m3sdk.test"
$TestClass = "net.m3mobile.m3sdk.DroidVncProjectMediaE2eTest"
$Instrumentation = "$TestPackage/androidx.test.runner.AndroidJUnitRunner"

function Invoke-Adb {
    param([string[]]$AdbArguments)

    $output = & adb -s $Serial @AdbArguments 2>&1 | Out-String
    if ($LASTEXITCODE -ne 0) {
        throw "adb failed: adb -s $Serial $($AdbArguments -join ' ')`n$output"
    }
    return $output.Trim()
}

function Package-Version {
    param([string]$PackageName)

    $dump = Invoke-Adb @("shell", "dumpsys", "package", $PackageName)
    $match = [regex]::Match($dump, "versionName=([^\s]+)")
    if (-not $match.Success) {
        throw "Cannot read versionName for $PackageName"
    }
    return $match.Groups[1].Value
}

function Project-Media-Mode {
    $raw = Invoke-Adb @("shell", "cmd", "appops", "get", $TargetPackage, "PROJECT_MEDIA")
    $match = [regex]::Match($raw, "PROJECT_MEDIA:\s*([A-Za-z_]+)")
    if (-not $match.Success) {
        return "unknown"
    }
    return $match.Groups[1].Value.ToLowerInvariant()
}

function StartUp-Process-Id {
    $output = & adb -s $Serial shell pidof $StartUpPackage 2>&1 | Out-String
    if ($LASTEXITCODE -ne 0) {
        return ""
    }
    return $output.Trim()
}

function Allowed-Log-After-Marker {
    param([string]$Marker)

    $logs = Invoke-Adb @(
        "logcat", "-d", "-v", "brief", "-s",
        "M3SDK_E2E:I", "StartUp:I", "*:S"
    )
    $markerIndex = $logs.LastIndexOf($Marker, [System.StringComparison]::Ordinal)
    if ($markerIndex -lt 0) {
        return $false
    }
    return $logs.Substring($markerIndex).Contains(
        "PROJECT_MEDIA allowed for net.christianbeier.droidvnc_ng"
    )
}

function Wait-For-Allowed-Log {
    param(
        [string]$Marker,
        [int]$TimeoutMilliseconds = 2000
    )

    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    do {
        if (Allowed-Log-After-Marker $Marker) {
            return $true
        }
        Start-Sleep -Milliseconds 100
    } while ($stopwatch.ElapsedMilliseconds -lt $TimeoutMilliseconds)
    return $false
}

function Require-Installed {
    param([string]$PackageName)

    $path = Invoke-Adb @("shell", "pm", "path", $PackageName)
    if (-not $path.StartsWith("package:")) {
        throw "$PackageName is not installed"
    }
}

$state = Invoke-Adb @("get-state")
if ($state -ne "device") {
    throw "Device $Serial is not ready: $state"
}

$model = Invoke-Adb @("shell", "getprop", "ro.product.model")
if (-not $model.Contains("SM24")) {
    throw "This E2E test requires SM24, actual model: $model"
}

Require-Installed $StartUpPackage
Require-Installed $TargetPackage
Require-Installed $ClientPackage
Require-Installed $TestPackage

$androidVersion = Invoke-Adb @("shell", "getprop", "ro.build.version.release")
$startUpVersion = Package-Version $StartUpPackage
$droidVncVersion = Package-Version $TargetPackage
if ($startUpVersion -ne "6.8.4") {
    throw "StartUp 6.8.4 is required, actual version: $startUpVersion"
}

if ([string]::IsNullOrWhiteSpace($ReportPath)) {
    $ReportPath = Join-Path $PSScriptRoot "..\app\build\reports\droidvnc-project-media-e2e-10.csv"
}
$ReportPath = [System.IO.Path]::GetFullPath($ReportPath)
$reportDirectory = Split-Path -Parent $ReportPath
New-Item -ItemType Directory -Force -Path $reportDirectory | Out-Null

$results = [System.Collections.Generic.List[object]]::new()

try {
    for ($run = 1; $run -le $Runs; $run++) {
        $startedAt = [DateTimeOffset]::Now
        $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
        $beforeMode = "unknown"
        $afterMode = "unknown"
        $pidBefore = ""
        $pidAfter = ""
        $instrumentationPassed = $false
        $allowedLogObserved = $false
        $detail = ""

        try {
            $pidBefore = StartUp-Process-Id
            if ([string]::IsNullOrWhiteSpace($pidBefore)) {
                throw "StartUp process is not running"
            }

            Invoke-Adb @(
                "shell", "cmd", "appops", "set",
                $TargetPackage, "PROJECT_MEDIA", "default"
            ) | Out-Null
            $beforeMode = Project-Media-Mode
            $logMarker = "DROIDVNC_E2E_RUN_{0:D3}_{1}" -f $run, [guid]::NewGuid()
            Invoke-Adb @("shell", "log", "-t", "M3SDK_E2E", $logMarker) | Out-Null

            $instrumentationOutput = & adb -s $Serial shell am instrument -w -r `
                -e class $TestClass $Instrumentation 2>&1 | Out-String
            $instrumentationPassed =
                $LASTEXITCODE -eq 0 -and
                $instrumentationOutput -match "OK \(1 test\)" -and
                $instrumentationOutput -notmatch "FAILURES!!!"

            $afterMode = Project-Media-Mode
            $pidAfter = StartUp-Process-Id
            $allowedLogObserved = Wait-For-Allowed-Log $logMarker

            $failures = [System.Collections.Generic.List[string]]::new()
            if ($beforeMode -ne "default") { $failures.Add("before=$beforeMode") }
            if (-not $instrumentationPassed) { $failures.Add("instrumentation failed") }
            if ($afterMode -ne "allow") { $failures.Add("after=$afterMode") }
            if ([string]::IsNullOrWhiteSpace($pidAfter)) { $failures.Add("StartUp stopped") }
            if ($pidBefore -ne $pidAfter) { $failures.Add("StartUp PID changed") }
            if (-not $allowedLogObserved) { $failures.Add("allow log missing") }
            $detail = $failures -join "; "
        } catch {
            $detail = $_.Exception.Message -replace "[\r\n]+", " "
        } finally {
            $stopwatch.Stop()
        }

        $passed = [string]::IsNullOrWhiteSpace($detail)
        $result = [pscustomobject]@{
            Run = $run
            StartedAt = $startedAt.ToString("o")
            DeviceSerial = $Serial
            DeviceModel = $model
            AndroidVersion = $androidVersion
            StartUpVersion = $startUpVersion
            DroidVncVersion = $droidVncVersion
            DurationMs = $stopwatch.ElapsedMilliseconds
            BeforeMode = $beforeMode
            CallbackAssertionsPassed = $instrumentationPassed
            AfterMode = $afterMode
            StartUpPidBefore = $pidBefore
            StartUpPidAfter = $pidAfter
            AllowedLogObserved = $allowedLogObserved
            Result = if ($passed) { "PASS" } else { "FAIL" }
            Detail = $detail
        }
        $results.Add($result)

        Write-Output (
            "RUN {0:D2}/{1:D2} {2} {3}ms before={4} after={5} pid={6}->{7}" -f
            $run, $Runs, $result.Result, $result.DurationMs,
            $beforeMode, $afterMode, $pidBefore, $pidAfter
        )
    }
} finally {
    Invoke-Adb @(
        "shell", "cmd", "appops", "set",
        $TargetPackage, "PROJECT_MEDIA", "allow"
    ) | Out-Null
}

$results | Export-Csv -Path $ReportPath -NoTypeInformation -Encoding UTF8
$passCount = @($results | Where-Object Result -eq "PASS").Count
$failCount = $Runs - $passCount
$duration = [math]::Round(($results | Measure-Object DurationMs -Sum).Sum / 1000, 3)

Write-Output "SUMMARY pass=$passCount fail=$failCount durationSeconds=$duration"
Write-Output "REPORT $ReportPath"
Write-Output "FINAL_MODE $(Project-Media-Mode)"

if ($failCount -gt 0) {
    exit 1
}
