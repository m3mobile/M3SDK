param(
    [string]$Serial = "2612031",
    [ValidateRange(1, 1000)]
    [int]$Runs = 10,
    [string]$ReportDirectory = "",
    [string]$VncProbePath = "C:\Users\M3\Documents\Codex\2026-07-23\7-droidvnc-ng-vnc-droidvnc-ng\work\test_vnc.ps1",
    [string]$VncPassword = "qq",
    [ValidateRange(1024, 65535)]
    [int]$LocalVncPort = 15901,
    [ValidateRange(0, 120)]
    [int]$AppOpsPersistenceWaitSeconds = 15
)

$ErrorActionPreference = "Stop"

$TargetPackage = "net.christianbeier.droidvnc_ng"
$StartUpPackage = "com.m3.startup"
$ClientPackage = "net.m3mobile.m3sdk"
$TestPackage = "net.m3mobile.m3sdk.test"
$TestClass = "net.m3mobile.m3sdk.DroidVncProjectMediaE2eTest"
$Instrumentation = "$TestPackage/androidx.test.runner.AndroidJUnitRunner"
$DialogPattern = @(
    "\uB290\uB9B0\u0020\uB300\uCCB4\u0020\uD654\uBA74\u0020\uCEA1\uCC98\u0020\uBAA8\uB4DC\u0020\uAC10\uC9C0",
    "\uC9C0\uAE08\u0020\uBE60\uB978\u0020\uBAA8\uB4DC\uB85C\u0020\uC5C5\uADF8\uB808\uC774\uB4DC\uD558\uC2DC\uACA0\uC2B5\uB2C8\uAE4C",
    "Slow fallback screen capture mode detected",
    "Do you want to upgrade to fast mode now"
) -join "|"
$FallbackLogPattern = @(
    "in fallback screen capture mode, asking for upgrade",
    "Showing dialog"
) -join "|"

function Invoke-Adb {
    param(
        [Parameter(Mandatory)]
        [string[]]$AdbArguments
    )

    $output = & adb -s $Serial @AdbArguments 2>&1 | Out-String
    if ($LASTEXITCODE -ne 0) {
        throw "adb failed: adb -s $Serial $($AdbArguments -join ' ')`n$output"
    }
    return $output.Trim()
}

function Invoke-Adb-AllowFailure {
    param(
        [Parameter(Mandatory)]
        [string[]]$AdbArguments
    )

    $previousPreference = $ErrorActionPreference
    $ErrorActionPreference = "SilentlyContinue"
    try {
        $output = & adb -s $Serial @AdbArguments 2>&1 | Out-String
        return [pscustomobject]@{
            ExitCode = $LASTEXITCODE
            Output = $output.Trim()
        }
    } finally {
        $ErrorActionPreference = $previousPreference
    }
}

function Require-Installed {
    param([string]$PackageName)

    $path = Invoke-Adb @("shell", "pm", "path", $PackageName)
    if (-not $path.StartsWith("package:")) {
        throw "$PackageName is not installed"
    }
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

function Wait-For-BootComplete {
    param([int]$TimeoutSeconds = 180)

    $waitResult = Invoke-Adb-AllowFailure @("wait-for-device")
    if ($waitResult.ExitCode -ne 0) {
        throw "wait-for-device failed: $($waitResult.Output)"
    }

    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    do {
        $result = Invoke-Adb-AllowFailure @("shell", "getprop", "sys.boot_completed")
        if ($result.ExitCode -eq 0 -and $result.Output -eq "1") {
            return $stopwatch.ElapsedMilliseconds
        }
        Start-Sleep -Seconds 2
    } while ($stopwatch.Elapsed.TotalSeconds -lt $TimeoutSeconds)

    throw "sys.boot_completed timeout after ${TimeoutSeconds}s"
}

function Wait-For-StartUpReady {
    param([int]$TimeoutSeconds = 60)

    $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
    do {
        $result = Invoke-Adb-AllowFailure @("shell", "pidof", $StartUpPackage)
        if ($result.ExitCode -eq 0 -and -not [string]::IsNullOrWhiteSpace($result.Output)) {
            # StartUpApplication initialization follows process creation. The observed
            # SM24 gap is about three seconds, so wait four seconds before one real API call.
            Start-Sleep -Seconds 4
            return [pscustomobject]@{
                ProcessId = $result.Output
                ReadyAfterMs = $stopwatch.ElapsedMilliseconds
            }
        }
        Start-Sleep -Seconds 1
    } while ($stopwatch.Elapsed.TotalSeconds -lt $TimeoutSeconds)

    throw "StartUp process timeout after ${TimeoutSeconds}s"
}

function Invoke-SdkE2eTest {
    $output = & adb -s $Serial shell am instrument -w -r `
        -e class $TestClass $Instrumentation 2>&1 | Out-String
    $passed =
        $LASTEXITCODE -eq 0 -and
        $output -match "OK \(1 test\)" -and
        $output -notmatch "FAILURES!!!"

    return [pscustomobject]@{
        Passed = $passed
        Output = $output.Trim()
    }
}

function Save-Adb-Output {
    param(
        [string[]]$AdbArguments,
        [string]$Path
    )

    Invoke-Adb $AdbArguments | Set-Content -LiteralPath $Path -Encoding UTF8
}

function Capture-VncFrame {
    param(
        [string]$RunDirectory,
        [int]$Attempts = 12
    )

    Invoke-Adb @("forward", "tcp:$LocalVncPort", "tcp:5901") | Out-Null
    $lastError = ""
    for ($attempt = 1; $attempt -le $Attempts; $attempt++) {
        try {
            $pngPath = Join-Path $RunDirectory "vnc-frame.png"
            $probeOutput = & $VncProbePath `
                -HostName "127.0.0.1" `
                -Port $LocalVncPort `
                -Password $VncPassword `
                -OutPng $pngPath 2>&1 | Out-String

            if ($LASTEXITCODE -ne 0) {
                throw "VNC probe exited with $LASTEXITCODE"
            }

            $probeOutput = $probeOutput.Trim()
            $probeOutput | Set-Content -LiteralPath (Join-Path $RunDirectory "vnc-frame.txt") -Encoding UTF8
            return [pscustomobject]@{
                Attempts = $attempt
                Output = $probeOutput
                PngPath = $pngPath
            }
        } catch {
            $lastError = $_.Exception.Message -replace "[\r\n]+", " "
            Start-Sleep -Seconds 3
        }
    }

    throw "VNC frame capture failed after $Attempts attempts: $lastError"
}

function Metric-Value {
    param(
        [string]$Text,
        [string]$Name
    )

    $match = [regex]::Match($Text, "(?m)^$([regex]::Escape($Name))=([^\r\n]+)\r?$")
    if (-not $match.Success) {
        return ""
    }
    return $match.Groups[1].Value.Trim()
}

function Boolean-Text {
    param([bool]$Value)

    if ($Value) { return "true" }
    return "false"
}

$state = Invoke-Adb @("get-state")
if ($state -ne "device") {
    throw "Device $Serial is not ready: $state"
}

$model = Invoke-Adb @("shell", "getprop", "ro.product.model")
if (-not $model.Contains("SM24")) {
    throw "This E2E test requires SM24, actual model: $model"
}

if (-not (Test-Path -LiteralPath $VncProbePath -PathType Leaf)) {
    throw "VNC probe script not found: $VncProbePath"
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

if ([string]::IsNullOrWhiteSpace($ReportDirectory)) {
    $ReportDirectory = Join-Path $PSScriptRoot "..\app\build\reports\droidvnc-fast-mode-reboot-e2e-10"
}
$ReportDirectory = [System.IO.Path]::GetFullPath($ReportDirectory)
New-Item -ItemType Directory -Force -Path $ReportDirectory | Out-Null
$reportPath = Join-Path $ReportDirectory "results.csv"
$summaryPath = Join-Path $ReportDirectory "summary.json"
$results = [System.Collections.Generic.List[object]]::new()
$suiteStopwatch = [System.Diagnostics.Stopwatch]::StartNew()

try {
    for ($run = 1; $run -le $Runs; $run++) {
        $startedAt = [DateTimeOffset]::Now
        $stopwatch = [System.Diagnostics.Stopwatch]::StartNew()
        $runDirectory = Join-Path $ReportDirectory ("run-{0:D2}" -f $run)
        New-Item -ItemType Directory -Force -Path $runDirectory | Out-Null

        $beforeMode = "unknown"
        $preRebootMode = "unknown"
        $postRebootMode = "unknown"
        $preRebootSdkPassed = $false
        $postRebootSdkPassed = $false
        $bootModeBeforeSdk = "unknown"
        $bootCompleteMs = 0
        $startUpReadyMs = 0
        $startUpPid = ""
        $vncAttempts = 0
        $frameWidth = 0
        $frameHeight = 0
        $framePixels = 0
        $nonBlackRatio = 0.0
        $sampledColors = 0
        $frameSha256 = ""
        $mediaProjectionActive = $false
        $virtualDisplayActive = $false
        $dialogTextAbsent = $false
        $dialogActivityAbsent = $false
        $fallbackUpgradeLogAbsent = $false
        $vncClientConnectedLogPresent = $false
        $detail = ""

        try {
            Write-Output ("RUN {0:D2}/{1:D2} phase=pre-reboot-sdk" -f $run, $Runs)
            Invoke-Adb @("shell", "cmd", "appops", "set", $TargetPackage, "PROJECT_MEDIA", "default") | Out-Null
            $beforeMode = Project-Media-Mode
            $preRebootSdk = Invoke-SdkE2eTest
            $preRebootSdk.Output | Set-Content -LiteralPath (Join-Path $runDirectory "sdk-pre-reboot.txt") -Encoding UTF8
            $preRebootSdkPassed = $preRebootSdk.Passed
            $preRebootMode = Project-Media-Mode
            if (-not $preRebootSdkPassed) {
                throw "Pre-reboot M3SDK instrumentation failed"
            }
            if ($beforeMode -ne "default" -or $preRebootMode -ne "allow") {
                throw "Pre-reboot AppOps transition failed: $beforeMode -> $preRebootMode"
            }

            Write-Output (
                "RUN {0:D2}/{1:D2} phase=appops-persistence wait={2}s" -f
                $run, $Runs, $AppOpsPersistenceWaitSeconds
            )
            Start-Sleep -Seconds $AppOpsPersistenceWaitSeconds
            if ((Project-Media-Mode) -ne "allow") {
                throw "PROJECT_MEDIA changed before reboot"
            }

            Invoke-Adb @("logcat", "-c") | Out-Null
            Write-Output ("RUN {0:D2}/{1:D2} phase=reboot" -f $run, $Runs)
            Invoke-Adb @("reboot") | Out-Null
            $bootCompleteMs = Wait-For-BootComplete
            $startUpReady = Wait-For-StartUpReady
            $startUpReadyMs = $startUpReady.ReadyAfterMs
            $startUpPid = $startUpReady.ProcessId
            $bootModeBeforeSdk = Project-Media-Mode

            Write-Output ("RUN {0:D2}/{1:D2} phase=post-reboot-sdk" -f $run, $Runs)
            $postRebootSdk = Invoke-SdkE2eTest
            $postRebootSdk.Output | Set-Content -LiteralPath (Join-Path $runDirectory "sdk-post-reboot.txt") -Encoding UTF8
            $postRebootSdkPassed = $postRebootSdk.Passed
            $postRebootMode = Project-Media-Mode
            if (-not $postRebootSdkPassed) {
                throw "Post-reboot M3SDK instrumentation failed"
            }
            if ($postRebootMode -ne "allow") {
                throw "Post-reboot PROJECT_MEDIA is $postRebootMode"
            }

            Invoke-Adb @("shell", "input", "keyevent", "KEYCODE_WAKEUP") | Out-Null
            Start-Sleep -Seconds 2
            Invoke-Adb @("shell", "wm", "dismiss-keyguard") | Out-Null
            Invoke-Adb @("shell", "input", "keyevent", "KEYCODE_HOME") | Out-Null
            Start-Sleep -Seconds 2

            Write-Output ("RUN {0:D2}/{1:D2} phase=vnc-frame" -f $run, $Runs)
            $vnc = Capture-VncFrame $runDirectory
            $vncAttempts = $vnc.Attempts
            $size = Metric-Value $vnc.Output "size"
            $sizeMatch = [regex]::Match($size, "^(\d+)x(\d+)$")
            if ($sizeMatch.Success) {
                $frameWidth = [int]$sizeMatch.Groups[1].Value
                $frameHeight = [int]$sizeMatch.Groups[2].Value
            }
            $pixelsText = Metric-Value $vnc.Output "pixels"
            if ($pixelsText -match "^\d+$") { $framePixels = [int]$pixelsText }
            $ratioText = Metric-Value $vnc.Output "nonBlackRatio"
            if ($ratioText -match "^[0-9.]+$") { $nonBlackRatio = [double]$ratioText }
            $colorsText = Metric-Value $vnc.Output "sampledColors"
            if ($colorsText -match "^\d+$") { $sampledColors = [int]$colorsText }
            $frameSha256 = Metric-Value $vnc.Output "frameSha256"

            Start-Sleep -Seconds 2
            Invoke-Adb @("shell", "uiautomator", "dump", "/data/local/tmp/droidvnc_fast_e2e.xml") | Out-Null
            Save-Adb-Output @("shell", "cat", "/data/local/tmp/droidvnc_fast_e2e.xml") (Join-Path $runDirectory "window.xml")
            Save-Adb-Output @("shell", "dumpsys", "media_projection") (Join-Path $runDirectory "media-projection.txt")
            Save-Adb-Output @("shell", "dumpsys", "display") (Join-Path $runDirectory "display.txt")
            Save-Adb-Output @("shell", "dumpsys", "activity", "activities") (Join-Path $runDirectory "activities.txt")
            Save-Adb-Output @(
                "logcat", "-d", "-v", "threadtime",
                "MainService:V", "MediaProjectionRequestActivity:V", "MediaProjectionService:V",
                "OnBootReceiver:V", "ActivityTaskManager:I", "DisplayDeviceRepository:I", "*:S"
            ) (Join-Path $runDirectory "logcat.txt")

            $ui = Get-Content -Raw -LiteralPath (Join-Path $runDirectory "window.xml")
            $mediaProjection = Get-Content -Raw -LiteralPath (Join-Path $runDirectory "media-projection.txt")
            $display = Get-Content -Raw -LiteralPath (Join-Path $runDirectory "display.txt")
            $activities = Get-Content -Raw -LiteralPath (Join-Path $runDirectory "activities.txt")
            $logs = Get-Content -Raw -LiteralPath (Join-Path $runDirectory "logcat.txt")

            $mediaProjectionActive = $mediaProjection -match "net\.christianbeier\.droidvnc_ng, uid=.*TYPE_SCREEN_CAPTURE"
            $virtualDisplayActive = $display -match "virtual:net\.christianbeier\.droidvnc_ng|droidVNC-NG"
            $dialogTextAbsent = $ui -notmatch $DialogPattern
            $dialogActivityAbsent = $activities -notmatch "MediaProjectionRequestActivity"
            $fallbackUpgradeLogAbsent = $logs -notmatch $FallbackLogPattern
            $vncClientConnectedLogPresent = $logs -match "onClientConnected: client"

            $failures = [System.Collections.Generic.List[string]]::new()
            if ($framePixels -le 0) { $failures.Add("no framebuffer pixels") }
            if ($nonBlackRatio -le 0.01) { $failures.Add("black framebuffer ratio=$nonBlackRatio") }
            if ($sampledColors -lt 10) { $failures.Add("insufficient colors=$sampledColors") }
            if (-not $mediaProjectionActive) { $failures.Add("MediaProjection inactive") }
            if (-not $virtualDisplayActive) { $failures.Add("virtual display inactive") }
            if (-not $dialogTextAbsent) { $failures.Add("upgrade dialog text present") }
            if (-not $dialogActivityAbsent) { $failures.Add("MediaProjectionRequestActivity present") }
            if (-not $fallbackUpgradeLogAbsent) { $failures.Add("fallback upgrade log present") }
            if ($bootModeBeforeSdk -ne "allow") { $failures.Add("boot AppOps mode=$bootModeBeforeSdk") }
            $detail = $failures -join "; "
        } catch {
            $detail = $_.Exception.Message -replace "[\r\n]+", " "
        } finally {
            $stopwatch.Stop()
            $restore = Invoke-Adb-AllowFailure @("shell", "cmd", "appops", "set", $TargetPackage, "PROJECT_MEDIA", "allow")
            if ($restore.ExitCode -ne 0 -and [string]::IsNullOrWhiteSpace($detail)) {
                $detail = "Failed to restore PROJECT_MEDIA allow: $($restore.Output)"
            }
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
            PreRebootSdkPassed = $preRebootSdkPassed
            PreRebootMode = $preRebootMode
            BootCompleteMs = $bootCompleteMs
            StartUpReadyMs = $startUpReadyMs
            StartUpPid = $startUpPid
            BootModeBeforeSdk = $bootModeBeforeSdk
            PostRebootSdkPassed = $postRebootSdkPassed
            PostRebootMode = $postRebootMode
            VncConnectAttempts = $vncAttempts
            FrameWidth = $frameWidth
            FrameHeight = $frameHeight
            FramePixels = $framePixels
            NonBlackRatio = $nonBlackRatio
            SampledColors = $sampledColors
            FrameSha256 = $frameSha256
            MediaProjectionActive = $mediaProjectionActive
            VirtualDisplayActive = $virtualDisplayActive
            DialogTextAbsent = $dialogTextAbsent
            DialogActivityAbsent = $dialogActivityAbsent
            FallbackUpgradeLogAbsent = $fallbackUpgradeLogAbsent
            VncClientConnectedLogPresent = $vncClientConnectedLogPresent
            Result = if ($passed) { "PASS" } else { "FAIL" }
            Detail = $detail
        }
        $results.Add($result)
        $results | Export-Csv -Path $reportPath -NoTypeInformation -Encoding UTF8

        Write-Output (
            "RUN {0:D2}/{1:D2} {2} {3}ms frame={4}x{5} nonBlack={6} colors={7} dialogAbsent={8} detail={9}" -f
            $run, $Runs, $result.Result, $result.DurationMs,
            $frameWidth, $frameHeight, $nonBlackRatio, $sampledColors,
            (Boolean-Text ($dialogTextAbsent -and $dialogActivityAbsent -and $fallbackUpgradeLogAbsent)),
            $detail
        )
    }
} finally {
    $suiteStopwatch.Stop()
    Invoke-Adb-AllowFailure @("shell", "cmd", "appops", "set", $TargetPackage, "PROJECT_MEDIA", "allow") | Out-Null
}

$passCount = @($results | Where-Object Result -eq "PASS").Count
$failCount = $Runs - $passCount
$summary = [ordered]@{
    completedAt = [DateTimeOffset]::Now.ToString("o")
    deviceSerial = $Serial
    deviceModel = $model
    androidVersion = $androidVersion
    startUpVersion = $startUpVersion
    droidVncVersion = $droidVncVersion
    runs = $Runs
    passed = $passCount
    failed = $failCount
    durationSeconds = [math]::Round($suiteStopwatch.Elapsed.TotalSeconds, 3)
    report = $reportPath
}
$summary | ConvertTo-Json | Set-Content -LiteralPath $summaryPath -Encoding UTF8

Write-Output "SUMMARY pass=$passCount fail=$failCount durationSeconds=$($summary.durationSeconds)"
Write-Output "REPORT $reportPath"
Write-Output "SUMMARY_JSON $summaryPath"
Write-Output "FINAL_MODE $(Project-Media-Mode)"

if ($failCount -gt 0) {
    exit 1
}
