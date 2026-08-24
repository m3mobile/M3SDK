# M3 SDK Xamarin Manual

NuGet package: [M3Mobile.M3Sdk.Xamarin 2.3.15](https://www.nuget.org/packages/M3Mobile.M3Sdk.Xamarin/2.3.15)


The M3 SDK Xamarin package provides C# APIs for configuring and controlling M3 Mobile devices from Xamarin.Android applications.

## Table of Contents
- [Requirements](#requirements)
- [Installation](#installation)
  - [1. Install NuGet Package](#1-install-nuget-package)
  - [2. Verify Package Reference](#2-verify-package-reference)
- [Basic Usage](#basic-usage)
  - [Accessing APIs](#accessing-apis)
  - [Async Reads and Callbacks](#async-reads-and-callbacks)
  - [Accessing API Groups](#accessing-api-groups)
  - [Strict Mode and Exception Handling](#strict-mode-and-exception-handling)
- [API](#api)
  - [Airplane Mode API](#airplane-mode-api)
    - [Turn on Airplane Mode](#turn-on-airplane-mode)
    - [Turn off Airplane Mode](#turn-off-airplane-mode)
  - [App API](#app-api)
    - [Install Local APK](#install-local-apk)
    - [Install Remote APK](#install-remote-apk)
    - [Enable Application](#enable-application)
    - [Disable Application](#disable-application)
    - [Run Application](#run-application)
    - [Run and Pin Application](#run-and-pin-application)
  - [Device API](#device-api)
    - [Set Media Volume](#set-media-volume)
    - [Set Ringtone Volume](#set-ringtone-volume)
    - [Set Notification Volume](#set-notification-volume)
    - [Set Alarm Volume](#set-alarm-volume)
    - [Enable Vibration Mode](#enable-vibration-mode)
    - [Disable Vibration Mode](#disable-vibration-mode)
    - [Set Display Settings](#set-display-settings)
    - [Get Serial Number](#get-serial-number)
    - [Lock Status Bar Expansion](#lock-status-bar-expansion)
    - [Unlock Status Bar Expansion](#unlock-status-bar-expansion)
    - [Get Bluetooth MAC Address](#get-bluetooth-mac-address)
  - [Language API](#language-api)
    - [Set Language](#set-language)
  - [Network API](#network-api)
    - [Set APN](#set-apn)
    - [Enable NFC](#enable-nfc)
    - [Disable NFC](#disable-nfc)
  - [Permission API](#permission-api)
    - [Grant Permission](#grant-permission)
    - [Revoke Permission](#revoke-permission)
    - [Allow PROJECT_MEDIA for an Application](#allow-projectmedia-for-an-application)
    - [Configure MediaProjection Screen-recording Indicator Exceptions](#configure-mediaprojection-screen-recording-indicator-exceptions)
  - [Quick Tile API](#quick-tile-api)
    - [Set Quick Tiles](#set-quick-tiles)
    - [Reset Quick Tiles](#reset-quick-tiles)
  - [Scanner API](#scanner-api)
    - [Start Scan](#start-scan)
    - [Stop Scan](#stop-scan)
    - [Get Scanner Status](#get-scanner-status)
    - [Get Scanner Type](#get-scanner-type)
    - [Scan Result Listener](#scan-result-listener)
    - [GS1 Parsed Listener](#gs1-parsed-listener)
    - [Digital Link Parsed Listener](#digital-link-parsed-listener)
    - [Scanner Settings](#scanner-settings)
    - [Floating Scanner Button UI](#floating-scanner-button-ui)
  - [KeyTool API](#keytool-api)
    - [Control Function-key Mode](#control-function-key-mode)
    - [Set Key Function](#set-key-function)
    - [Control Home and Recent Buttons](#control-home-and-recent-buttons)
    - [Control Scan-key Wake-Up](#control-scan-key-wake-up)
  - [AppCenter Kiosk API](#appcenter-kiosk-api)
    - [Change Kiosk Admin Password](#change-kiosk-admin-password)
    - [Keep Admin Mode While Screen Is Off](#keep-admin-mode-while-screen-is-off)
  - [StartUp Setting API](#startup-setting-api)
    - [Reset StartUp Settings](#reset-startup-settings)
  - [Time API](#time-api)
    - [Set Date and Time](#set-date-and-time)
    - [Set NTP Server](#set-ntp-server)
    - [Set Timezone](#set-timezone)
    - [Get NTP Server](#get-ntp-server)
    - [Get NTP Interval](#get-ntp-interval)
    - [Get Timezone](#get-timezone)
  - [Usb API](#usb-api)
    - [Set USB Mode to MTP](#set-usb-mode-to-mtp)
    - [Set USB Mode to RNDIS](#set-usb-mode-to-rndis)
    - [Set USB Mode to MIDI](#set-usb-mode-to-midi)
    - [Set USB Mode to PTP](#set-usb-mode-to-ptp)
    - [Disable USB Data (Charging Only)](#disable-usb-data-charging-only)
    - [Get Current USB Modes](#get-current-usb-modes)
  - [Wifi API](#wifi-api)
    - [Get Wi-Fi MAC Address](#get-wi-fi-mac-address)
    - [Get Factory Wi-Fi MAC Address](#get-factory-wi-fi-mac-address)
    - [Set Wi-Fi Enabled](#set-wi-fi-enabled)
    - [Captive Portal Detection](#captive-portal-detection)
    - [Frequency Band Control](#frequency-band-control)
    - [Set Wi-Fi Country](#set-wi-fi-country)
    - [Open Network Notification](#open-network-notification)
    - [Roaming Configuration](#roaming-configuration)
      - [Set Roaming Trigger](#set-roaming-trigger)
      - [Set Roaming Delta](#set-roaming-delta)
    - [Wi-Fi Sleep Policy](#wi-fi-sleep-policy)
    - [Wi-Fi Stability](#wi-fi-stability)
    - [Set Wi-Fi Channels](#set-wi-fi-channels)
    - [Network Management](#network-management)
      - [Set Access Point](#set-access-point)
      - [Clear Saved Wi-Fi Networks](#clear-saved-wi-fi-networks)
      - [Remove Wi-Fi Network](#remove-wi-fi-network)
    - [Device Specific Wi-Fi Settings](#device-specific-wi-fi-settings)
      - [Get Roaming Threshold](#get-roaming-threshold)
      - [Get Roaming Delta](#get-roaming-delta)
      - [Get Wi-Fi Frequency Band](#get-wi-fi-frequency-band)
      - [Get Wi-Fi Country Code](#get-wi-fi-country-code)

## Requirements

*   **MonoAndroid**: 7.0 or later
*   **Visual Studio**: 2017 or later

## Installation

### 1. Install NuGet Package

Search for `M3Mobile.M3Sdk.Xamarin` in Visual Studio NuGet Package Manager, or run the following command in Package Manager Console.

```powershell
Install-Package M3Mobile.M3Sdk.Xamarin -Version 2.3.15
```

The NuGet package page is linked at the top of this document.

### 2. Verify Package Reference

The project file should contain the following package reference.

```xml
<PackageReference Include="M3Mobile.M3Sdk.Xamarin" Version="2.3.15" />
```

## Basic Usage

The Xamarin package creates an SDK instance from an Android `Context`. It is recommended to create one instance and reuse it in your application.

### Accessing APIs

All functions are available through an `IM3Sdk` instance.

```csharp
using Android.App;
using M3Sdk.Xamarin;

// Example: Turn on Airplane Mode
IM3Sdk m3 = M3Mobile.Create(Application.Context);
m3.TurnOnAirplaneMode();
```

Call `Dispose()` when the SDK instance is no longer needed.

```csharp
m3.Dispose();
```

### Async Reads and Callbacks

Some StartUp and ScanEmul read APIs provide both `Task`-based async methods and callback methods. `Async` methods also provide `CancellationToken` overloads.

Callback methods use the `M3RequestCallback<T>` delegate. On success, `error` is `null`. On failure, `result` is the default value and `error` contains the exception. Callback requests return `IM3Cancelable`, which provides `Cancel()`, `IsCancellationRequested`, and `Dispose()`. Canceled callback requests do not invoke the callback afterward.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string serialNumber = await m3.GetSerialNumberAsync();
string serialNumberWithCancel = await m3.GetSerialNumberAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetSerialNumber((result, error) =>
{
    if (error != null)
        return;

    string value = result;
});

request.Cancel();
request.Dispose();
```

### Accessing API Groups

You can call APIs directly from the root `IM3Sdk` instance or through feature-specific group properties.

```csharp
using System.Collections.Generic;
using M3Sdk.Xamarin.ScanEmul;

m3.StartUp.SetWifiCountry("KR");
m3.ScanEmul.SetScannerReadMode(ReadMode.Multiple);
m3.AppCenter.SetKeepAdminModeOnSleep(true);

string ntpServer = m3.Time.GetNtpServer();
IList<string> usbModes = m3.Usb.GetCurrentUsbModes();
int roamingDelta = m3.Wifi.GetRoamingDelta();
```

`IM3Sdk` exposes `StartUp`, `ScanEmul`, `KeyTool`, `AppCenter`, `Time`, `Wifi`, and `Usb` groups. Their types are `IStartUpApi`, `IScanEmulApi`, `IKeyToolApi`, `IAppCenterApi`, `ITimeApi`, `IWifiApi`, and `IUsbApi`. `M3Mobile.Create(...)` returns the public root implementation `M3Sdk` as `IM3Sdk`, and the group implementations are `StartUpApi`, `ScanEmulApi`, `KeyToolApi`, `AppCenterApi`, `TimeApi`, `WifiApi`, and `UsbApi`. The `ScanEmul` group implements `IDisposable` to clean up internal scan connections. Calling `IM3Sdk.Dispose()` cleans it up together.

### Strict Mode and Exception Handling

The M3 SDK provides a **Strict Mode** that affects how API calls behave when required conditions, such as device support or app versions, are not met.

**How it works:**

*   **Enabled**: When Strict Mode is enabled, API calls corresponding to the following Java/Kotlin SDK condition annotations throw exceptions if the condition is not met.
    *   `@SupportedModels`: Specifies the device models for which this API is available.
    *   `@UnsupportedModels`: Specifies the device models for which this API is not available.
    *   `@RequiresStartUp`: Indicates that a specific version or later of StartUp must be installed.
    *   `@RequiresScanEmul`: Indicates that a specific version or later of ScanEmul must be installed.

    The following exceptions may occur:
    *   `UnsupportedDeviceModelException`: Thrown if an API is called on an unsupported device model.
    *   `UnsatisfiedVersionException`: Thrown if the installed StartUp, ScanEmul, AppCenter, or KeyTool app version is lower than the API requirement. AppCenter kiosk and `com.m3.keytoolsl20`-based KeyTool API version checks always run, regardless of Strict Mode.
    *   `KeyToolAppUnavailableException`: Thrown when the KeyTool companion app required by an API is not installed or is not visible. This check always runs because KeyTool requests are one-way broadcasts.


*   **Disabled**: In this mode, API calls that do not meet the required conditions are **ignored automatically**. No exception is thrown, so the application continues running.

**Enabling Strict Mode:**

Strict Mode is disabled by default.

To enable Strict Mode, add the following `<meta-data>` tag inside the `<application>` tag of the application's `AndroidManifest.xml`.

```xml
<application ...>
    <meta-data
        android:name="M3_STRICT_MODE"
        android:value="true" />
</application>
```

It is recommended to enable Strict Mode during development and testing to catch potential issues early. In production, choose silent failure or explicit exception handling according to your application's error handling strategy.


**Common Direct Broadcast Examples**

Use the action, target package, and typed extras shown in each API's `Direct Broadcast` table. In an MDM console such as AirWatch or SOTI, enter the same values in the corresponding broadcast fields.

```csharp
// Implicit broadcast
var implicitRequest = new Intent("ACTION_FROM_THIS_MANUAL");
implicitRequest.PutExtra("extra_key", "extra_value");
context.SendBroadcast(implicitRequest);

// Explicit broadcast
var explicitRequest = new Intent("ACTION_FROM_THIS_MANUAL");
explicitRequest.SetPackage("TARGET_PACKAGE_FROM_THIS_MANUAL");
explicitRequest.PutExtra("extra_key", true);
context.SendBroadcast(explicitRequest);
```
---

## API

### Airplane Mode API

Controls the device's Airplane Mode.

#### Turn on Airplane Mode

Turns on airplane mode.

*   **Requires StartUp Version**: `6.3.7` or later

```csharp
m3.TurnOnAirplaneMode();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `airplane` |
| `airplane` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Turn off Airplane Mode

Turns off airplane mode.

*   **Requires StartUp Version**: `6.3.7` or later

```csharp
m3.TurnOffAirplaneMode();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `airplane` |
| `airplane` | `Boolean` | O | `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


---

### App API

Installs applications, enables or disables specific packages, runs applications, and controls app pinning.

#### Install Local APK

Installs an APK from a local file path. The existing one-parameter overload keeps the original
behavior. Additional overloads can permit a same-`versionCode` reinstall and launch the installed
application after installation succeeds.

*   **Required StartUp version**:
    *   Path only: `6.2.14` or later
    *   `allowSameVersionUpdate`: `6.8.1` or later
    *   `launchAfterInstall`: `6.8.2` or later
*   **Parameters**:
    *   `filePath` (string): The absolute path to the .apk file to install.
    *   `allowSameVersionUpdate` (bool): Reinstalls when the APK `versionCode` matches the installed app.
    *   `launchAfterInstall` (bool): Launches the installed app only after installation succeeds.

```csharp
m3.InstallLocalApk(filePath);
m3.InstallLocalApk(filePath, allowSameVersionUpdate: true);
m3.InstallLocalApk(
    filePath,
    allowSameVersionUpdate: true,
    launchAfterInstall: true);
```

Use the two-parameter overload when only same-version reinstall support is needed on StartUp 6.8.1.
The three-parameter overload always requires StartUp 6.8.2, even when `launchAfterInstall` is `false`.

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `apk_install` |
| `type` | `Int` | O | `0` |
| `path` | `String` | O | Absolute APK path |
| `allow_same_version_update` | `Boolean` | X | Whether to reinstall the same versionCode |
| `launch_after_install` | `Boolean` | X | Whether to launch after successful installation |

Both optional extras default to `false` when omitted.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Install Remote APK

Downloads and installs an APK from a remote URL. It provides the same reinstall and post-install
launch options as local APK installation.

*   **Required StartUp version**:
    *   URL only: `6.2.14` or later
    *   `allowSameVersionUpdate`: `6.8.1` or later
    *   `launchAfterInstall`: `6.8.2` or later
*   **Parameters**:
    *   `url` (string): The URL of the APK file.
    *   `allowSameVersionUpdate` (bool): Reinstalls when the APK `versionCode` matches the installed app.
    *   `launchAfterInstall` (bool): Launches the installed app only after installation succeeds.

```csharp
m3.InstallRemoteApk(url);
m3.InstallRemoteApk(url, allowSameVersionUpdate: true);
m3.InstallRemoteApk(
    url,
    allowSameVersionUpdate: true,
    launchAfterInstall: true);
```

APK installation requests are one-way broadcasts. A normal return confirms only that M3SDK sent
the request. It does not confirm download, installation, or launch success. StartUp launches the
application only after `PackageInstaller` reports success and supplies an installed package name.
It does not launch when download or installation fails, the package name is unavailable, or a
same-version installation is skipped. Verify the StartUp notification and logs, the installed
package, and the launched screen.

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `apk_install` |
| `type` | `Int` | O | `1` |
| `url` | `String` | O | APK download URL |
| `allow_same_version_update` | `Boolean` | X | Whether to reinstall the same versionCode |
| `launch_after_install` | `Boolean` | X | Whether to launch after successful installation |

Both optional extras default to `false` when omitted.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Enable Application

Enables a specified application package.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application to enable.

```csharp
m3.EnableApp(packageName);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `application` |
| `package_name` | `String` | O | Target application package |
| `enable` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Disable Application

Disables a specified application package.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application to disable.

```csharp
m3.DisableApp(packageName);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `application` |
| `package_name` | `String` | O | Target application package |
| `enable` | `Boolean` | O | `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Run Application

Enables and runs a specified application package.

*   **Requires StartUp Version**: `6.8.0` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application to enable and run.
*   **Notes**:
    *   This API automatically enables the package before launching it. You do not need to call `EnableApp` first.
    *   `EnableApp` only enables the package and does not launch it.

```csharp
m3.RunApp(packageName);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `application` |
| `package_name` | `String` | O | Target application package |
| `enable` | `Boolean` | O | `true` |
| `auto_run` | `Boolean` | O | `true` |
| `pin_app` | `Boolean` | O | `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Run and Pin Application

Enables, runs, and pins a specified application package.

*   **Requires StartUp Version**: `6.8.0` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application to enable, run, and pin.
*   **Prerequisites**:
    *   Screen Pinning must be enabled in the OS/StartUp environment.
*   **Notes**:
    *   This API automatically enables the package before launching and pinning it. You do not need to call `EnableApp` first.
    *   StartUp 6.8.0 does not provide an SDK or broadcast API to stop app pinning. To exit the pinned app manually, press the Home button 10 times in a row.

```csharp
m3.RunAndPinApp(packageName);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `application` |
| `package_name` | `String` | O | Target application package |
| `enable` | `Boolean` | O | `true` |
| `auto_run` | `Boolean` | O | `true` |
| `pin_app` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.



---

### Device API

Controls various device settings such as volume, display, and vibration.

#### Set Media Volume

Sets the media volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**: 0 to 15
*   **Parameters**:
    *   `value` (int): The desired volume level.

```csharp
m3.SetMediaVolume(value);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `volume` |
| `volume_media` | `Int` | O | Volume value passed to the SDK |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Ringtone Volume

Sets the ringtone volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**:
    *   0 to 15: Models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `SL25`, `PC10`
    *   0 to 7: All other models
*   **Parameters**:
    *   `value` (int): The desired volume level.

```csharp
m3.SetRingtoneVolume(value);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `volume` |
| `volume_ringtone` | `Int` | O | Volume value passed to the SDK |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Notification Volume

Sets the notification volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**: Same as `SetRingtoneVolume`
*   **Parameters**:
    *   `value` (int): The desired volume level.

```csharp
m3.SetNotificationVolume(value);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `volume` |
| `volume_notification` | `Int` | O | Volume value passed to the SDK |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Alarm Volume

Sets the alarm volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**:
    *   0 to 15: Models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `PC10`
    *   0 to 7: All other models
*   **Parameters**:
    *   `value` (int): The desired volume level.

```csharp
m3.SetAlarmVolume(value);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `volume` |
| `volume_alarm` | `Int` | O | Volume value passed to the SDK |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Enable Vibration Mode

Enables vibration mode. This sets ringtone and notification volumes to 0.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.EnableVibrationMode();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `volume` |
| `volume_vibrator` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Disable Vibration Mode

Disables vibration mode.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.DisableVibrationMode();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `volume` |
| `volume_vibrator` | `Boolean` | O | `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Display Settings

Configures display settings.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `displaySetting` (DisplaySetting): Object containing display configuration.

```csharp
using System;
using M3Sdk.Xamarin.Startup;

var displaySetting = new DisplaySetting(
    enableAutoBrightness: false,
    brightness: 180,
    enableAutoRotate: true,
    rotateForce: RotateForce.Automatic,
    enableScreenLock: true,
    sleepMode: SleepMode.Minutes5,
    policyControl: PolicyControl.Default,
    showBatteryPercentage: true,
    screenSaverMode: ScreenSaverMode.Never,
    screenSaverComponent: "");

m3.SetDisplaySetting(displaySetting);
```

Main `DisplaySetting` enums:

*   `SleepMode`: `Seconds15`, `Seconds30`, `Minutes1`, `Minutes2`, `Minutes5`, `Minutes10`, `Minutes30`, `Never`
*   `RotateForce`: `Default`, `Automatic`, `Landscape`, `LandscapeReverse`, `LandscapeSensor`, `Portrait`, `PortraitReverse`, `PortraitSensor`
*   `PolicyControl`: `HideStatusBar`, `HideNavigationBar`, `HideSystemBar`, `Default`
*   `ScreenSaverMode`: `WhileCharging`, `WhileDocked`, `WhileChargingOrDocked`, `Never`

`DisplaySetting` exposes `EnableAutoBrightness`, `Brightness`, `EnableAutoRotate`, `RotateForce`, `EnableScreenLock`, `SleepMode`, `PolicyControl`, `ShowBatteryPercentage`, `ScreenSaverMode`, and `ScreenSaverComponent`.

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `display` |
| `display_auto_brightness` | `Boolean` | X | Auto brightness |
| `display_brightness_step` | `Int` | X | `1..255` |
| `display_auto_rotate` | `Boolean` | X | Auto rotation |
| `display_rotate_force` | `Int` | X | `0..7` |
| `display_disable_screen_lock` | `Boolean` | X | Whether to disable screen lock |
| `display_sleep` | `Int` | X | Screen timeout in ms or `2147483647` |
| `display_policy_control` | `Int` | X | `1..4` |
| `display_battery_percentage` | `Int` | X | Show `1`, hide `2` |
| `display_screensaver_mode` | `Int` | X | `0..3` |
| `display_screensaver_component` | `String` | X | Component name |

Send only the extras to change in addition to `setting`. Omitted fields retain their current system values.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Get Serial Number

Retrieves the device's serial number.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Returns**: The serial number string.

```csharp
using System;
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string serialNumber = await m3.GetSerialNumberAsync();
string serialNumberWithCancel = await m3.GetSerialNumberAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetSerialNumber((result, error) =>
{
    if (error != null)
        return;

    string serialNumberFromCallback = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `get_serial` |

*   **Response action**: `com.android.server.startupservice.system.response`

| Response extra | Type | Value |
|---|---|---|
| `get_serial` | `String` | Serial number |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


#### Lock Status Bar Expansion

Locks status bar expansion so the user cannot pull down notifications or quick settings.

*   **Requires StartUp Version**: `6.4.12` or later

```csharp
m3.LockStatusBarExpansion();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `status_bar` |
| `prevent` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Unlock Status Bar Expansion

Unlocks status bar expansion.

*   **Requires StartUp Version**: `6.4.12` or later

```csharp
m3.UnlockStatusBarExpansion();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `status_bar` |
| `prevent` | `Boolean` | O | `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Get Bluetooth MAC Address

Retrieves the Bluetooth MAC address of the device.

*   **Supported Models**: All models
*   **Requires StartUp Version**: `6.5.31` or later (UL30), `6.5.35` or later (other models)
*   **Returns**: The Bluetooth MAC address string (format: XX:XX:XX:XX:XX:XX).

```csharp
using System;
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string bluetoothMac = await m3.GetBluetoothMacAsync();
string bluetoothMacWithCancel = await m3.GetBluetoothMacAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetBluetoothMac((result, error) =>
{
    if (error != null)
        return;

    string bluetoothMacFromCallback = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `get_bluetooth_mac` |

*   **Response action**: `com.android.server.startupservice.system.response`

| Response extra | Type | Value |
|---|---|---|
| `get_bluetooth_mac` | `String` | Bluetooth MAC address |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


---

### Language API

Controls the system language settings.

#### Set Language

Sets the device's system language and country.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `language` (string): The language code (e.g., "en", "ko").
    *   `country` (string): The country code (e.g., "US", "KR").

```csharp
m3.SetLanguage("ko", "KR");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `language` |
| `language_value` | `String` | O | `language-country`, for example `ko-KR` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


---

### Network API

Configures mobile network settings.

#### Set APN

Sets the Access Point Name (APN) configuration.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `apn` (Apn): The APN object containing configuration details. Use `Apn.CreateBuilder()` to create an instance.

```csharp
using M3Sdk.Xamarin.Startup;

var apn = Apn.CreateBuilder()
    .SetName("M3")
    .SetUrl("internet")
    .SetMcc("450")
    .SetMnc("08")
    .SetType("default")
    .Build();

m3.SetApn(apn);
```

`Apn` can be created with its constructor or `Apn.Builder`. Core properties are `Name`, `Url`, `Mcc`, `Mnc`, and `Type`. Optional values include `Proxy`, `Port`, `User`, `Password`, `Server`, `Mmsc`, `MmsProxy`, `MmsPort`, `AuthType`, `Protocol`, `Roaming`, `Mvno`, and `MvnoValue`. Builder methods are `SetName`, `SetUrl`, `SetMcc`, `SetMnc`, `SetType`, `SetProxy`, `SetPort`, `SetUser`, `SetPassword`, `SetServer`, `SetMmsc`, `SetMmsProxy`, `SetMmsPort`, `SetAuthType`, `SetProtocol`, `SetRoaming`, `SetMvno`, `SetMvnoValue`, and `Build`.

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `apn` |
| `apn_name` | `String` | O | APN name |
| `apn_url` | `String` | O | APN URL |
| `apn_mcc` | `String` | O | MCC |
| `apn_mnc` | `String` | O | MNC |
| `apn_type` | `String` | O | APN type |
| `apn_proxy` | `String` | X | Proxy |
| `apn_port` | `String` | X | Port |
| `apn_user` | `String` | X | User |
| `apn_password` | `String` | X | Password |
| `apn_server` | `String` | X | Server |
| `apn_mmsc` | `String` | X | MMSC |
| `apn_mms_proxy` | `String` | X | MMS proxy |
| `apn_mms_port` | `String` | X | MMS port |
| `apn_auth_type` | `Int` | X | Auth type |
| `apn_protocol` | `Int` | X | Protocol |
| `apn_roaming` | `Int` | X | Roaming protocol |
| `apn_mvno` | `Int` | X | MVNO type |
| `apn_mvno_value` | `String` | X | MVNO value |

Omitted optional `String` extras are handled as `null`; omitted optional `Int` extras default to `0`.

Immediately after the setting request, send an additional `com.android.server.startupservice.config.fin` broadcast.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Enable NFC

Enables Near Field Communication (NFC).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Requires Android Version**: Android 11 (R) or later

```csharp
m3.EnableNfc();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `nfc` |
| `nfc_on` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Disable NFC

Disables Near Field Communication (NFC).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Requires Android Version**: Android 11 (R) or later

```csharp
m3.DisableNfc();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `nfc` |
| `nfc_on` | `Boolean` | O | `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


---

### Permission API

Grants or revokes runtime permissions for applications.

#### Grant Permission

Grants a specific runtime permission to a target application package.

*   **Requires StartUp Version**: `6.4.17` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application.
    *   `permission` (string): The fully qualified name of the permission (e.g., `android.permission.CAMERA`).

```csharp
m3.GrantPermission("com.example.app", "android.permission.CAMERA");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `permission` |
| `package` | `String` | O | Target application package |
| `permission` | `String` | O | Android permission |
| `permission_mode` | `Int` | O | `1` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Revoke Permission

Revokes a specific runtime permission from a target application package.

*   **Requires StartUp Version**: `6.4.17` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application.
    *   `permission` (string): The fully qualified name of the permission to revoke.

```csharp
m3.RevokePermission("com.example.app", "android.permission.CAMERA");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `permission` |
| `package` | `String` | O | Target application package |
| `permission` | `String` | O | Android permission |
| `permission_mode` | `Int` | O | `2` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Allow PROJECT_MEDIA for an Application

Allows the Android `PROJECT_MEDIA` app operation for an installed screen-capture application. The
caller supplies the package name, so the API is not limited to DroidVNC-NG.

*   **Requires StartUp Version**: `6.8.4` or later
*   **Supported Model**: `SM24` currently returns `Success`; other models return `UnsupportedDevice`.
*   **Parameter**: `packageName` is the exact package name of an already installed application.

```csharp
ProjectMediaResult result = await m3.AllowProjectMediaAsync(
    "net.christianbeier.droidvnc_ng");

if (result.IsSuccess)
    Android.Util.Log.Info("ProjectMedia", "PROJECT_MEDIA allowed");
else
    Android.Util.Log.Error("ProjectMedia", result.Status + ": " + result.ErrorMessage);
```

A callback overload is also available:

```csharp
IM3Cancelable request = m3.AllowProjectMedia(packageName, (result, error) =>
{
    if (error != null)
        Android.Util.Log.Error("ProjectMedia", error.ToString());
    else
        Android.Util.Log.Info("ProjectMedia", result.Status + " (" + (int)result.Status + ")");
});
```

`ProjectMediaResult` represents a StartUp feature outcome. A faulted task or callback `error`
represents a transport failure, such as a broadcast failure or response timeout, and has no
`ProjectMediaStatus`. An unrecognized response code is normalized to `ApplyFailed`, with the raw
code included in `ErrorMessage`.

| Code | `ProjectMediaStatus` | Meaning | Recommended action |
|---:|---|---|---|
| 0 | `Success` | StartUp allowed the operation and verified `MODE_ALLOWED`. | Start or connect the screen-capture application. |
| 1 | `UnsupportedDevice` | The current model does not support the feature. | Use SM24 or a StartUp build that explicitly supports the model. |
| 2 | `TargetNotInstalled` | The requested package is not installed. | Install the application and retry with its exact package name. |
| 3 | `InvalidTarget` | The package name is blank, or the package and UID do not match. | Check the package name and resolved UID. |
| 4 | `PermissionDenied` | StartUp cannot control AppOps with its current system privileges. | Check firmware signing, shared UID, and AppOps privileges. |
| 5 | `AppOpUnavailable` | The required AppOps API is unavailable. | Check Android framework and StartUp compatibility. |
| 6 | `ApplyFailed` | Applying the mode failed, or readback was not `MODE_ALLOWED`. | Inspect StartUp logs and the package, UID, and AppOps state. |

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: `com.m3.startup`

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `project_media` |
| `project_media_package` | `String` | O | Installed target package name |
| `project_media_messenger` | `Messenger` | O | Messenger receiving the result |

The result is returned through `project_media_messenger`. `Message.what` contains the
`ProjectMediaStatus` code, and `project_media_error_message` may contain additional failure
details.

#### Configure MediaProjection Screen-recording Indicator Exceptions

Hides the SM24 status-bar screen-recording indicator while selected packages use MediaProjection.
This feature is separate from `AllowProjectMediaAsync()`, which grants the recording AppOps permission.

*   **Requires StartUp Version**: `6.8.7` or later
*   **Supported Model**: `SM24`
*   **Behavior**: This is a one-way request. StartUp removes blanks and duplicates, persists the list, and restores it after app or device restart.

```csharp
// Replace the complete list.
m3.SetMediaProjectionIndicatorExemptPackages(
    "net.christianbeier.droidvnc_ng",
    "com.example.recorder");

// Add to or remove from the existing list.
m3.AddMediaProjectionIndicatorExemptPackages("com.example.support");
m3.RemoveMediaProjectionIndicatorExemptPackages("com.example.recorder");

// Clear the complete list.
m3.ClearMediaProjectionIndicatorExemptPackages();
```

Calling `SetMediaProjectionIndicatorExemptPackages()` without package names clears the list. No
processing response is returned. The request is sent immediately, but the status-bar screen-recording
indicator for an active MediaProjection session may not refresh immediately. For deterministic
verification, stop and restart the active session. On SM24, expanding and collapsing the notification
shade may also refresh the indicator. A device reboot is not required.

| API | Existing-list behavior |
|---|---|
| `SetMediaProjectionIndicatorExemptPackages` | Replaces the complete list |
| `AddMediaProjectionIndicatorExemptPackages` | Appends packages and removes duplicates |
| `RemoveMediaProjectionIndicatorExemptPackages` | Removes only the supplied packages |
| `ClearMediaProjectionIndicatorExemptPackages` | Clears the complete list |

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: `com.m3.startup`

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `media_projection_exempt_packages` |
| `mode` | `String` | X | `replace`, `append`, `remove`, `clear`; defaults to `replace` when omitted |
| `packages` | `String` or `ArrayList<String>` | Conditional | Targets for `replace`, `append`, or `remove`; omit for `clear` |

> This is a one-way request. Sending the broadcast does not guarantee that StartUp saved the list or applied the system property.

---

### Quick Tile API

Customizes the Quick Settings tiles in the System UI.

#### Set Quick Tiles

Sets the Quick Tiles to be displayed.

*   **Requires StartUp Version**: `6.4.1` or later
*   **Parameters**:
    *   `quickTiles` (params QuickTile[]): One or more `QuickTile` objects to add.

```csharp
using M3Sdk.Xamarin.Startup;

m3.SetQuickTiles(
    new QuickTile(QuickTileId.Wifi, "Wi-Fi"),
    new QuickTile(QuickTileId.Bluetooth, "Bluetooth"),
    new QuickTile(QuickTileId.Flashlight, "Flashlight"));
```

`QuickTile` exposes `Id` and `Name`. `QuickTileId` provides `Wifi`, `Bluetooth`, `Flashlight`, `DoNotDisturb`, `AutoRotation`, `BatterySaver`, `AirplaneMode`, `NightLight`, `ScreenRecord`, `QrCodeScanner`, `Alarm`, `DeviceControls`, `Wallet`, `ScreenCast`, `Location`, `Hotspot`, `ColorInversion`, `DataSaver`, and `DarkTheme`.

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `quick_tile` |
| `quick_tile_action` | `String` | O | `add` |
| `quick_tile_items` | `String` | O | JSON array string |

Immediately after the setting request, send an additional `com.android.server.startupservice.config.fin` broadcast.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Reset Quick Tiles

Resets the Quick Tiles configuration to the default state.

*   **Requires StartUp Version**: `6.4.1` or later

```csharp
m3.ResetQuickTile();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `quick_tile` |
| `quick_tile_action` | `String` | O | `reset` |
| `quick_tile_items` | `String` | O | `[]` |

Immediately after the setting request, send an additional `com.android.server.startupservice.config.fin` broadcast.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


---

### Scanner API

Controls the barcode scanner and configures scanning preferences.

#### Start Scan

Starts the scanning process.

*   **Requires ScanEmul Version**: `2.13.0` or later

```csharp
m3.StartScan();
```

**Direct Broadcast**

*   **Action**: `android.intent.action.M3SCANNER_BUTTON_DOWN`
*   **Target package**: Not set (implicit broadcast)
*   **Extra**: None

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Stop Scan

Stops the scanning process.

*   **Requires ScanEmul Version**: `2.13.0` or later

```csharp
m3.StopScan();
```

**Direct Broadcast**

*   **Action**: `android.intent.action.M3SCANNER_BUTTON_UP`
*   **Target package**: Not set (implicit broadcast)
*   **Extra**: None

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Get Scanner Status

Retrieves the current status of the scanner.

*   **Requires ScanEmul Version**: `2.13.0` or later
*   **Returns**: An integer representing the status.
    *   `1`: Failed to open
    *   `2`: Failed to close
    *   `4`: Succeeded to open
    *   `8`: Succeeded to close

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
int status = await m3.GetScannerStatusAsync();
int statusWithCancel = await m3.GetScannerStatusAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetScannerStatus((result, error) =>
{
    if (error != null)
        return;

    int scannerStatus = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.m3onoff.ison`
*   **Target package**: Not set (implicit broadcast)
*   **Extra**: None

*   **Response action**: `scanemul.action.status`

| Response extra | Type | Value |
|---|---|---|
| `scanemul.extra.status` | `Int` | `1`, `2`, `4`, `8` |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


#### Get Scanner Type

Retrieves the scanner hardware type.

*   **Requires ScanEmul Version**: `2.13.0` or later
*   **Returns**: The scanner type string.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string scannerType = await m3.GetScannerTypeAsync();
string scannerTypeWithCancel = await m3.GetScannerTypeAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetScannerType((result, error) =>
{
    if (error != null)
        return;

    string scannerTypeFromCallback = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.m3onoff.ison`
*   **Target package**: Not set (implicit broadcast)
*   **Extra**: None

*   **Response action**: `scanemul.action.status`

| Response extra | Type | Value |
|---|---|---|
| `m3scanner_module_type` | `String` | Scanner module type |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


#### Scan Result Listener

Registers or unregisters a listener to receive scan results.

*   **Requires ScanEmul Version**: `4.11.0` or later
*   **Parameters**:
    *   `listener` (IOnScanResultListener or Action<ScanResult>): The listener that receives scan results.
*   **Event Args**: `ScanResultEventArgs`
*   **Interface Callback Method**: `OnScanResult`
*   **Received Data**:
    *   `ScanResult.Barcode`: The decoded barcode data.
    *   `ScanResult.Type`: The scanner code type.
    *   `ScanResult.RawData`: Raw scanner data bytes.

```csharp
using System;
using M3Sdk.Xamarin.ScanEmul;

// Event
m3.ScanResultReceived += (sender, args) =>
{
    ScanResult result = args.Result;
    string barcode = result.Barcode;
    string type = result.Type;
};

// Action registration. Dispose the returned IDisposable to unregister it.
IDisposable registration = m3.RegisterOnScanResultListener(result =>
{
    string barcode = result.Barcode;
});

registration.Dispose();
```

Interface-based listeners are also available.

```csharp
m3.RegisterOnScanResultListener(listener);
m3.UnregisterOnScanResultListener(listener);
```

**Direct Broadcast**

No direct command broadcast is available. Results use the ScanEmul message connection, so the SDK or a receiving application is required.


#### GS1 Parsed Listener

Registers or unregisters a listener to receive GS1 parsed scan results.

*   **Requires ScanEmul Version**: `4.11.0` or later
*   **Parameters**:
    *   `listener` (IOnGS1ParsedListener or Action<IList<GS1ParsedData>>): The listener that receives parsed GS1 data.
*   **Event Args**: `GS1ParsedEventArgs`
*   **Interface Callback Method**: `OnGS1Parsed`
*   **Received Data**:
    *   `GS1ParsedData.Ai`: Application Identifier.
    *   `GS1ParsedData.Data`: Parsed data value.
    *   `GS1ParsedData.Description`: Application Identifier description.

```csharp
using System;
using System.Collections.Generic;
using M3Sdk.Xamarin.ScanEmul;

// Event
m3.GS1ParsedReceived += (sender, args) =>
{
    IList<GS1ParsedData> result = args.Result;
};

// Action registration
IDisposable registration = m3.RegisterOnGS1ParsedListener(result =>
{
    foreach (GS1ParsedData item in result)
    {
        string ai = item.Ai;
        string data = item.Data;
    }
});

registration.Dispose();
```

Interface-based listeners are also available.

```csharp
m3.RegisterOnGS1ParsedListener(listener);
m3.UnregisterOnGS1ParsedListener(listener);
```

**Direct Broadcast**

No direct command broadcast is available. Results use the ScanEmul message connection, so the SDK or a receiving application is required.


#### Digital Link Parsed Listener

Registers or unregisters a listener to receive Digital Link parsed scan results.

*   **Requires ScanEmul Version**: `4.11.0` or later
*   **Parameters**:
    *   `listener` (IOnDigitalLinkParsedListener or Action<IList<DigitalLinkParsedData>>): The listener that receives parsed Digital Link data.
*   **Event Args**: `DigitalLinkParsedEventArgs`
*   **Interface Callback Method**: `OnDigitalLinkParsed`
*   **Received Data**:
    *   `DigitalLinkParsedData.Ai`: Application Identifier.
    *   `DigitalLinkParsedData.Data`: Parsed data value.
    *   `DigitalLinkParsedData.Description`: Application Identifier description.

```csharp
using System;
using System.Collections.Generic;
using M3Sdk.Xamarin.ScanEmul;

// Event
m3.DigitalLinkParsedReceived += (sender, args) =>
{
    IList<DigitalLinkParsedData> result = args.Result;
};

// Action registration
IDisposable registration = m3.RegisterOnDigitalLinkParsedListener(result =>
{
    foreach (DigitalLinkParsedData item in result)
    {
        string ai = item.Ai;
        string data = item.Data;
    }
});

registration.Dispose();
```

Interface-based listeners are also available.

```csharp
m3.RegisterOnDigitalLinkParsedListener(listener);
m3.UnregisterOnDigitalLinkParsedListener(listener);
```

**Direct Broadcast**

No direct command broadcast is available. Results use the ScanEmul message connection, so the SDK or a receiving application is required.


#### Scanner Settings

Configures various scanner options. These settings apply to the currently active profile.

*   **Requires ScanEmul Version**: `2.11.0` or later

##### Feedback

```csharp
using M3Sdk.Xamarin.ScanEmul;

// Sound
m3.SetScanSound(ScanSound.Beep);
// Enum: None, Beep, DingDong

// Vibration
m3.EnableScanVibration();
m3.DisableScanVibration();

// LED
m3.EnableScanLed();
m3.DisableScanLed();
m3.SetScanLedTime(timeMillis); // Range: 1 to 1000
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: Not set (implicit broadcast)

| Operation | `setting` | Extra | Type | Value |
|---|---|---|---|---|
| Sound | `sound` | `sound_mode` | `Int` | None `0`, BEEP `1`, DING_DONG `2` |
| Vibration | `vibration` | `vibration_value` | `Int` | Disable `0`, enable `1` |
| LED | `led` | `led_value` | `Int` | Disable `0`, enable `1` |
| LED time | `led_time` | `led_time_value` | `Int` | `1..1000` ms |

> These are one-way requests. Sending a broadcast does not guarantee that a setting was applied. Verify the resulting state separately in the MDM.


##### Scanning Mode

```csharp
using System.Threading;
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.ScanEmul;

CancellationToken cancellationToken = CancellationToken.None;

m3.SetScannerReadMode(ReadMode.Multiple);
// Enum: AimingAndRelease, Async, Continue, Multiple, Presentation, Sync

// Task
ReadMode readMode = await m3.GetScannerReadModeAsync();
ReadMode readModeWithCancel = await m3.GetScannerReadModeAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetScannerReadMode((result, error) =>
{
    if (error != null)
        return;

    ReadMode mode = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: Not set (implicit broadcast)

| Operation | `setting` | Extra | Type | Value |
|---|---|---|---|---|
| Read mode SET | `read_mode` | `read_mode_value` | `Int` | ASYNC `0`, SYNC `1`, CONTINUE `2`, MULTIPLE `3`, PRESENTATION `4`, AIMING_AND_RELEASE `5` |

*   **Response action**: `com.android.server.scannerservice.setting`

| Response extra | Type | Value |
|---|---|---|
| `m3scanner_read_mode` | `Int` | Current read mode `0..5` |

> SET requests are one-way. GET results require an Android application that listens for the response action; a typical MDM web console may not receive them.

GET sends action `com.android.server.scannerservice.getsetting` without extras.


##### Output Configuration

```csharp
using M3Sdk.Xamarin.ScanEmul;

// Output Mode
m3.SetScanResultOutputMode(OutputMode.CopyAndPaste);
// Enum: CommitText, CopyAndPaste, CopyToClipboard, KeyEmulation

// Formatting
m3.SetScanResultPrefix("Prefix");
m3.SetScanResultPostfix("Postfix");
m3.SetScanResultEndCharacter(EndCharacter.Enter);
// Enum: Enter, KeyboardEnter, KeyboardSpace, KeyboardTab, None, Space, Tab
```

Read APIs provide both `Task` and callback forms.

```csharp
using System.Threading;
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.ScanEmul;

CancellationToken cancellationToken = CancellationToken.None;

// Task
OutputMode outputMode = await m3.GetScanResultOutputModeAsync();
OutputMode outputModeWithCancel = await m3.GetScanResultOutputModeAsync(cancellationToken);

string prefix = await m3.GetScanResultPrefixAsync();
string prefixWithCancel = await m3.GetScanResultPrefixAsync(cancellationToken);

string postfix = await m3.GetScanResultPostfixAsync();
string postfixWithCancel = await m3.GetScanResultPostfixAsync(cancellationToken);

EndCharacter endCharacter = await m3.GetScanResultEndCharacterAsync();
EndCharacter endCharacterWithCancel = await m3.GetScanResultEndCharacterAsync(cancellationToken);

// Callback
IM3Cancelable outputModeRequest = m3.GetScanResultOutputMode((result, error) => { });
IM3Cancelable prefixRequest = m3.GetScanResultPrefix((result, error) => { });
IM3Cancelable postfixRequest = m3.GetScanResultPostfix((result, error) => { });
IM3Cancelable endCharacterRequest = m3.GetScanResultEndCharacter((result, error) => { });
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: Not set (implicit broadcast)

| Operation | `setting` | Extra | Type | Value |
|---|---|---|---|---|
| Output mode SET | `output_mode` | `output_mode_value` | `Int` | COPY_AND_PASTE `0`, KEY_EMULATION `1`, COPY_TO_CLIPBOARD `2`, COMMIT_TEXT `3` |
| Prefix SET | `prefix` | `prefix_value` | `String` | Prefix string |
| Postfix SET | `postfix` | `postfix_value` | `String` | Postfix string |
| End character SET | `end_char` | `end_char_value` | `Int` | ENTER `0`, SPACE `1`, TAB `2`, KEYBOARD_ENTER `3`, KEYBOARD_SPACE `4`, KEYBOARD_TAB `5`, NONE `6` |

*   **Response action**: `com.android.server.scannerservice.setting`

| Response extra | Type | Value |
|---|---|---|
| `m3scanner_output_mode` | `Int` | Current output mode |
| `m3scanner_prefix` | `String` | Current prefix |
| `m3scanner_postfix` | `String` | Current postfix |
| `m3scanner_endchar` | `Int` | Current end character |

> SET requests are one-way. GET results require an Android application that listens for the response action; a typical MDM web console may not receive them.

GET sends action `com.android.server.scannerservice.getsetting` without extras.


##### Profile Status

Checks if the current scanner profile is enabled.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
bool enabled = await m3.IsScannerProfileEnabledAsync();
bool enabledWithCancel = await m3.IsScannerProfileEnabledAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.IsScannerProfileEnabled((result, error) =>
{
    if (error != null)
        return;

    bool isEnabled = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.getsetting`
*   **Target package**: Not set (implicit broadcast)
*   **Extra**: None

*   **Response action**: `com.android.server.scannerservice.setting`

| Response extra | Type | Value |
|---|---|---|
| `is_enable` | `Boolean` | Whether the current profile is enabled |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


#### Floating Scanner Button UI

Sets or retrieves the ScanEmul default scanner button image, opacity, and size on supported devices.

*   **Supported models**: All models except `WD10` (ScanEmul is not available on `WD10`)
*   **Required ScanEmul version**: `4.15.1` or later (`4.14.10` or later on `SM24`)

```csharp
var options = new ScannerButtonUiOptions(
    imagePath: "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png",
    opacityPercent: 80,
    size: ScannerButtonUiSize.Large);

ScannerButtonUiVerificationResult result =
    await m3.SetAndVerifyScannerButtonUiAsync(options);
```

**Direct Broadcast**

*   **Action**: `com.android.server.scannerservice.settingchange`
*   **Target package**: `net.m3mobile.app.scanemul`

| Operation | `setting` | Extra | Type | Value |
|---|---|---|---|---|
| SET | `scanner_button_ui` | `request_id` | `String` | Response correlation ID; optional |
| SET | `scanner_button_ui` | `scanner_button_image_path` | `String` | Absolute image path; empty string selects the default image; optional |
| SET | `scanner_button_ui` | `scanner_button_opacity_percent` | `Int` | `20..100`; optional |
| SET | `scanner_button_ui` | `scanner_button_size` | `String` | `extra_small`, `small`, `medium`, `large`, or `extra_large`; optional |

*   **Response action**: `com.android.server.scannerservice.setting`

| Response extra | Type | Value |
|---|---|---|
| `setting` | `String` | `scanner_button_ui` |
| `request_id` | `String` | Request ID |
| `success` | `Boolean` | Whether the value was saved |
| `status` | `String` | ScanEmul processing status |
| `runtime_applied` | `Boolean` | Whether the running UI was updated |
| `scanner_button_image_path` | `String` | Saved image path |
| `scanner_button_opacity_percent` | `Int` | Saved opacity |
| `scanner_button_size` | `String` | Saved size |

ScanEmul sends a response broadcast for both SET and GET. A typical MDM web console may not receive it, so verify the actual UI state when only request delivery can be observed.

GET uses the following contract.

*   **Action**: `com.android.server.scannerservice.getsetting`
*   **Target package**: `net.m3mobile.app.scanemul`

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `scanner_button_ui` |
| `request_id` | `String` | X | Response correlation ID |

If `request_id` is omitted, the response has no correlation ID. SET must include at least one of image path, opacity, or size; omitted UI fields keep their current values. ScanEmul `4.15.1` or later is required (`4.14.10` or later on `SM24`).

An application that handles the response directly must register a dynamic receiver for the response action and match `request_id`.

```csharp
string requestId = Guid.NewGuid().ToString();
var receiver = new ScannerButtonUiReceiver(requestId);
var filter = new IntentFilter("com.android.server.scannerservice.setting");
if (Build.VERSION.SdkInt >= BuildVersionCodes.Tiramisu)
    context.RegisterReceiver(receiver, filter, ReceiverFlags.Exported);
else
    context.RegisterReceiver(receiver, filter);

var request = new Intent("com.android.server.scannerservice.getsetting")
    .SetPackage("net.m3mobile.app.scanemul")
    .PutExtra("setting", "scanner_button_ui")
    .PutExtra("request_id", requestId);
context.SendOrderedBroadcast(request, null);

// Unregister the receiver from timeout handling when no response arrives.
```

Receiver class:

```csharp
sealed class ScannerButtonUiReceiver : BroadcastReceiver
{
    private readonly string requestId;

    public ScannerButtonUiReceiver(string requestId)
    {
        this.requestId = requestId;
    }

    public override void OnReceive(Context? context, Intent? intent)
    {
        if (intent?.GetStringExtra("setting") != "scanner_button_ui")
            return;
        if (intent.GetStringExtra("request_id") != requestId)
            return;

        bool success = intent.GetBooleanExtra("success", false);
        string? status = intent.GetStringExtra("status");
        bool runtimeApplied = intent.GetBooleanExtra("runtime_applied", false);
        context?.UnregisterReceiver(this);
    }
}
```


---
### KeyTool API

Controls physical key configuration through KeyTool companion apps. The methods are available both
from the flat SDK facade and from the `KeyTool` API group.

> **One-way request:** KeyTool broadcasts do not return an acknowledgement. A normal return means
> only that Android accepted the request. Verify the physical key or wake-up behavior after the call.
> If the required package is unavailable, the SDK throws `KeyToolAppUnavailableException`.
> APIs based on `com.m3.keytoolsl20` also verify the minimum version regardless of Strict Mode.
> An older version causes `UnsatisfiedVersionException` with method, model, package, current, and
> required versions.

| SDK feature | Models | Package | Minimum version |
|---|---|---|---|
| Function-key mode | `SL20K` | `com.m3.keytoolsl20` | `1.2.6` |
| Set key function | `SL20`, `SL20K`, `SL20P`, `SL25`, `WD10` | `com.m3.keytoolsl20` | `1.2.6` |
| Set key function | `SM24` | `com.m3.keytoolsl20` | `1.3.8` |
| Set key function | `SM25` | `com.m3.keytoolsl20` | `1.3.16` |
| Set key function + Wake-Up | `SM24` | `com.m3.keytoolsl20` | `1.3.8` |
| Home/Recent control | `SM24`, `SM25` | `com.m3.keytoolsl20` | `1.4.1` |
| Scan-key Wake-Up | `SL20P` | `net.m3.keytool` | Unverified; package check only |
| Scan-key Wake-Up | `SM24` | `com.m3.keytoolsl20` | `1.3.8` |

The SM24 Scan Wake-Up minimum is `1.3.8`; `1.3.9` or later is recommended for field deployment
because it includes service-connection stabilization. Product suffixes such as `1.4.1_alpha`,
`1.3.4F`, and `1.4.0AD` are compared using the leading number of each version segment.

#### Control Function-key Mode

*   **Supported model**: `SL20K`
*   **Required package**: `com.m3.keytoolsl20` version `1.2.6` or later

```csharp
using IM3Sdk m3 = M3Mobile.Create(Application.Context);

m3.EnableFn();
m3.DisableFn();
m3.LockFn();

// The grouped form is also available.
m3.KeyTool.EnableFn();
```

**Direct Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_CONTROL_FN_STATE`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `fn_state` | `Int` | O | Disable `0`, enable `1`, lock `2` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

Requires KeyTool SL20 `1.2.6` or later on SL20K.


#### Set Key Function

Assigns a KeyTool function title to a physical key title.

*   **Supported models**: `SL20`, `SL20K`, `SL20P`, `SL25`, `WD10`, `SM24`, `SM25`
*   **Required package**: `com.m3.keytoolsl20` (`1.2.6` for `SL20`/`SL20K`/`SL20P`/`SL25`/`WD10`,
    `1.3.8` for `SM24`, and `1.3.16` for `SM25`)

```csharp
try
{
    m3.SetKeyFunction("Left Scan", "Volume Up");
    // REQUEST_SENT_UNVERIFIED: verify the physical key on the device.
}
catch (Exception error)
{
    Android.Util.Log.Error("M3SDK", error.ToString());
}
```

Use the current KeyTool title spelling, including `Volume Up` and `Volume Down`. KeyTool 1.4.1
also normalizes settings saved by older releases as `Volume up` or `Volume down`.

On SM24, the three-argument overload includes the key mapping and Wake-Up state in one
`ACTION_SET_KEY` request.

```csharp
m3.SetKeyFunction(
    "Left Scan",
    "Scan",
    true);
```

The request sends `key_title`, `key_function`, and `key_wakeup` together. KeyTool applies the
mapping and then the Wake-Up state sequentially; it does not roll both changes back as one
transaction. A normal return therefore does not prove that both settings were applied.

**Direct Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_SET_KEY`
*   **Target package**: `com.m3.keytoolsl20`
*   **Delivery**: Ordered broadcast

| Extra | Type | Required | Value |
|---|---|---|---|
| `key_title` | `String` | O | KeyTool key title |
| `key_function` | `String` | O | KeyTool function title |
| `key_wakeup` | `Boolean` | X | Use on SM24 to also change Wake-Up in the same request |

Omitting `key_wakeup` leaves the existing Wake-Up setting unchanged.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

Minimum KeyTool SL20 versions are `1.2.6` for SL20/SL20K/SL20P/SL25/WD10, `1.3.8` for SM24, and `1.3.16` for SM25. The overload that includes `key_wakeup` requires SM24 and version `1.3.8` or later.


#### Control Home and Recent Buttons

*   **Supported models**: `SM24`, `SM25`
*   **Required package**: `com.m3.keytoolsl20` version `1.4.1` or later

```csharp
m3.EnableHomeButton();
m3.DisableHomeButton();
m3.EnableRecentButton();
m3.DisableRecentButton();

// The grouped form is also available.
m3.KeyTool.DisableRecentButton();
```

These are one-way requests. Verify the actual navigation button after each call.

**Direct Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_SET_KEY`
*   **Target package**: `com.m3.keytoolsl20`

| Operation | `key_title` (`String`, required) | `key_function` (`String`, required) |
|---|---|---|
| Enable Home | `Home` | `Default` |
| Disable Home | `Home` | `Disable` |
| Enable Recent | `Recent` | `Default` |
| Disable Recent | `Recent` | `Disable` |

> These are one-way requests. Sending a broadcast does not guarantee that a setting was applied. Verify the resulting state separately in the MDM.

Send an ordered broadcast. KeyTool SL20 `1.4.1` or later is required.


#### Control Scan-key Wake-Up

Controls whether the left or right scan key wakes an `SL20P` or `SM24` device.

*   **Supported models**: `SL20P`, `SM24`
*   **SL20P protocol**: explicit `WAKEUP_CONTROL_LEFT` or `WAKEUP_CONTROL_RIGHT` broadcast to
    `net.m3.keytool`
*   **SM24 protocol**: explicit `ACTION_SET_KEY` broadcast to `com.m3.keytoolsl20` version `1.3.8`
    or later (`1.3.9` or later recommended)

The current model, rather than installed-package priority, selects the protocol. SM24 never uses
the deprecated `WAKEUP_CONTROL_*` actions even when `net.m3.keytool` is installed. SL20P keeps the
Legacy protocol even when `com.m3.keytoolsl20` is installed.

```csharp
m3.EnableLeftScanWakeUp();
m3.DisableLeftScanWakeUp();
m3.EnableRightScanWakeUp();
m3.DisableRightScanWakeUp();
```

The published-package sample displays installed KeyTool package versions and reports one-way calls
as `REQUEST_SENT_UNVERIFIED` rather than success.

**SL20P left**

**Direct Broadcast**

*   **Action**: `net.m3.keytool.WAKEUP_CONTROL_LEFT`
*   **Target package**: `net.m3.keytool`

| Extra | Type | Required | Value |
|---|---|---|---|
| `wakeup_enable` | `Boolean` | O | `true` or `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

Use only for the SL20P left scan key.

**SL20P right**

**Direct Broadcast**

*   **Action**: `net.m3.keytool.WAKEUP_CONTROL_RIGHT`
*   **Target package**: `net.m3.keytool`

| Extra | Type | Required | Value |
|---|---|---|---|
| `wakeup_enable` | `Boolean` | O | `true` or `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

Use only for the SL20P right scan key.

**SM24**

**Direct Broadcast**

*   **Action**: `com.m3.keytoolsl20.ACTION_SET_KEY`
*   **Target package**: `com.m3.keytoolsl20`
*   **Delivery**: Ordered broadcast

| Extra | Type | Required | Value |
|---|---|---|---|
| `key_title` | `String` | O | `Left Scan` or `Right Scan` |
| `key_wakeup` | `Boolean` | O | `true` or `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

Use only on SM24. KeyTool SL20 `1.3.8` or later is required and `1.3.9` or later is recommended. Do not send `key_function` when changing only Wake-Up.


---

### AppCenter Kiosk API

Controls AppCenter kiosk administrator features through one-way explicit broadcasts. Methods are
available both from the flat SDK facade and from the `AppCenter` API group.

> **One-way request:** AppCenter broadcasts do not return an acknowledgement. A normal return means
> only that Android accepted the request. It does not prove that AppCenter applied the setting.
> AppCenter `2.2.0` or later must be installed and able to receive broadcasts. The SDK verifies
> AppCenter availability and version regardless of Strict Mode.

#### Change Kiosk Admin Password

*   **Requires AppCenter Version**: `2.2.0` or later
*   **Parameters**:
    *   `currentPassword`: Current administrator password. Empty strings are rejected.
    *   `newPassword`: New administrator password. Length must be 4 to 20 characters.

The SDK does not trim either password. If the current password is wrong, AppCenter may ignore the
request and the SDK cannot confirm the result.

```csharp
m3.ChangeKioskAdminPassword(currentPassword, newPassword);

// The grouped form is also available.
m3.AppCenter.ChangeKioskAdminPassword(currentPassword, newPassword);
```

**Direct Broadcast**

*   **Action**: `com.m3.appcenter.ACTION_CHANGE_PASSWORD`
*   **Target package**: `com.m3.appcenter`

| Extra | Type | Required | Value |
|---|---|---|---|
| `com.m3.appcenter.EXTRA_CURRENT_PASSWORD` | `String` | O | Current administrator password |
| `com.m3.appcenter.EXTRA_NEW_PASSWORD` | `String` | O | New password, 4 to 20 characters |
| `com.m3.appcenter.EXTRA_ENCRYPTION_ENABLED` | `Boolean` | O | `true` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

AppCenter `2.2.0` or later is required.


#### Keep Admin Mode While Screen Is Off

*   **Requires AppCenter Version**: `2.2.0` or later
*   **Parameters**:
    *   `enabled`: `true` keeps administrator mode after screen off. `false` restores the normal
        user-mode behavior and may require administrator login again.

Administrator mode is not preserved after reboot.

```csharp
m3.SetKeepAdminModeOnSleep(true);
m3.SetKeepAdminModeOnSleep(false);

// The grouped form is also available.
m3.AppCenter.SetKeepAdminModeOnSleep(true);
```

**Direct Broadcast**

*   **Action**: `com.m3.appcenter.ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP`
*   **Target package**: `com.m3.appcenter`

| Extra | Type | Required | Value |
|---|---|---|---|
| `com.m3.appcenter.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP` | `Int` | O | Keep `1`, disable `0` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

AppCenter `2.2.0` or later is required.


---


### StartUp Setting API

Manages the StartUp SDK's own settings.

#### Reset StartUp Settings

Resets the StartUp settings to their default values.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.ResetStartUpSetting();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `option` |
| `option_reset` | `Boolean` | O | `true` |

Immediately after the setting request, send an additional `com.android.server.startupservice.config.fin` broadcast.

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


---

### Time API

Configures system time, timezone, and NTP server settings.

#### Set Date and Time

Sets the date and time of the device.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `dateTime` (DateTime): The date and time to set.
    *   `dateTime` (DateTimeOffset): The date and time offset value to set.
    *   `year`, `month`, `day`, `hour`, `minute`, `second` (int): Individual date and time fields.

```csharp
using System;

m3.SetDateTime(DateTime.Now);
m3.SetDateTime(DateTimeOffset.Now);
m3.SetDateTime(2026, 5, 27, 10, 30, 0);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `datetime` |
| `date` | `String` | O | `yyyy-MM-dd` |
| `time` | `String` | O | `HH:mm:ss` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set NTP Server

Sets the NTP server for automatic time synchronization. This setting takes effect after the next reboot.

*   **Requires StartUp Version**: `6.4.9` or later
*   **Parameters**:
    *   `host` (string): The hostname or IP address of the NTP server.

```csharp
m3.SetNtpServer("time.android.com");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `ntp` |
| `ntp_server` | `String` | O | NTP host |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Timezone

Sets the system's default timezone.

*   **Requires StartUp Version**: `6.5.9` or later
*   **Parameters**:
    *   `timezone` (string): The timezone identifier (e.g., "Asia/Seoul", "America/New_York").

```csharp
m3.SetTimeZone("Asia/Seoul");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `timezone` |
| `timezone` | `String` | O | IANA timezone ID |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Get NTP Server

Retrieves the currently configured NTP server address.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP server address string.

```csharp
string ntpServer = m3.GetNtpServer();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


#### Get NTP Interval

Retrieves the currently configured NTP synchronization interval.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP synchronization interval in milliseconds (int).

```csharp
int ntpInterval = m3.GetNtpInterval();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


#### Get Timezone

Retrieves the system's current default timezone.

*   **Returns**: The timezone identifier string.

```csharp
string timeZone = m3.GetTimeZone();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


---

### Usb API

Configures the USB connection mode of the device.

#### Set USB Mode to MTP

Sets the USB connection mode to MTP (Media Transfer Protocol).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeMtp();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `mtp` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set USB Mode to RNDIS

Sets the USB connection mode to RNDIS (USB Tethering).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeRndis();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `rndis` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set USB Mode to MIDI

Sets the USB connection mode to MIDI.

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeMidi();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `midi` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set USB Mode to PTP

Sets the USB connection mode to PTP (Picture Transfer Protocol).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModePtp();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `ptp` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Disable USB Data (Charging Only)

Disables all USB data connections, setting the mode to charging only.

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeNone();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `usb_setting` |
| `usb_mode` | `String` | O | `none` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Get Current USB Modes

Retrieves the current USB connection mode.

*   **Returns**: A list of strings representing the currently active USB modes. Returns an empty list if no active modes are found.

```csharp
using System.Collections.Generic;

IList<string> usbModes = m3.GetCurrentUsbModes();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


---

### Wifi API

Provides comprehensive control over Wi-Fi settings and configurations.

#### Get Wi-Fi MAC Address

Retrieves the Wi-Fi MAC address of the device.

*   **Requires StartUp Version**: `6.4.11` or later
*   **Returns**: The Wi-Fi MAC address string.

```csharp
using System.Threading;
using M3Sdk.Xamarin;

CancellationToken cancellationToken = CancellationToken.None;

// Task
string wifiMac = await m3.GetWifiMacAsync();
string wifiMacWithCancel = await m3.GetWifiMacAsync(cancellationToken);

// Callback
IM3Cancelable request = m3.GetWifiMac((result, error) =>
{
    if (error != null)
        return;

    string wifiMacFromCallback = result;
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `get_wifi_mac` |

*   **Response action**: `com.android.server.startupservice.system.response`

| Response extra | Type | Value |
|---|---|---|
| `get_wifi_mac` | `String` | Wi-Fi MAC address |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


#### Get Factory Wi-Fi MAC Address

Retrieves the factory Wi-Fi MAC address of the device. This API is different from `GetWifiMacAsync()`: it uses the StartUp factory MAC API and can return the device factory Wi-Fi MAC before the device has connected to a Wi-Fi AP.

The device does not need to be connected to a Wi-Fi AP, but Wi-Fi must be turned on. If Wi-Fi is turned off, StartUp may not be able to return the factory Wi-Fi MAC address.

*   **Requires StartUp Version**: `6.7.3` or later
*   **Returns**: `FactoryWifiMacResult`

```csharp
using Android.Content;
using System.Threading;
using M3Sdk.Xamarin;
using M3Sdk.Xamarin.Startup;

CancellationToken cancellationToken = CancellationToken.None;

// Task
FactoryWifiMacResult result = await m3.GetFactoryWifiMacAsync();
FactoryWifiMacResult resultWithCancel = await m3.GetFactoryWifiMacAsync(cancellationToken);

if (result.IsSuccess)
{
    string factoryWifiMac = result.MacAddress;
}
else
{
    string error = result.ErrorMessage;
}

// Callback
IM3Cancelable request = m3.GetFactoryWifiMac((callbackResult, error) =>
{
    if (error != null)
        return;

    if (callbackResult.IsSuccess)
    {
        string factoryWifiMacFromCallback = callbackResult.MacAddress;
    }
});
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `get_factory_wifi_mac` |

*   **Response action**: `com.android.server.startupservice.system.response`

| Response extra | Type | Value |
|---|---|---|
| `get_factory_wifi_mac` | `String` | Factory Wi-Fi MAC address |
| `get_factory_wifi_mac_success` | `Boolean` | Whether the lookup succeeded |
| `get_factory_wifi_mac_error_message` | `String` | Failure detail |

> A typical MDM web console may not receive the response broadcast. Use an Android application that listens for the response action when a result is required.


#### Set Wi-Fi Enabled

Enables or disables Wi-Fi on the device.

This API is handled by StartUp. On Android 10 or later, a general Android app cannot control Wi-Fi directly; StartUp must be deployed as a system or privileged app.

*   **Requires StartUp Version**: `6.8.5` or later (`6.8.3` or later on `SM24`)
*   **Supported Models**: `SM20`, `SL20`, `SL20P`, `SL20K`, `US20`, `US30`, `UL20` (including `UL20F/W/WF`), `UL30`, `SM24`, `SM25`, `PC10`, `WD10`
*   **Parameters**:
    *   `enabled` (bool): `true` to enable Wi-Fi, `false` to disable Wi-Fi.

```csharp
m3.SetWifiEnabled(true);
m3.SetWifiEnabled(false);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_enabled` |
| `enabled` | `Boolean` | O | `true` or `false` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Captive Portal Detection

Controls whether the device detects captive portals (login pages for public Wi-Fi).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL20`

```csharp
m3.EnableCaptivePortalDetection();
m3.DisableCaptivePortalDetection();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `captive_portal` |
| `value` | `Int` | O | Enable `1`, disable `0` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Frequency Band Control

Restricts the Wi-Fi frequency band usage.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SM15`, `SL10`, `SL10K`

```csharp
m3.AllowAllWifiFrequencyBand();          // Allow all bands
m3.AllowOnly2_4GHzWifiFrequencyBand();   // Allow only the 2.4 GHz band
m3.AllowOnly5GHzWifiFrequencyBand();     // Allow only the 5 GHz band
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_freq_band` |
| `value` | `Int` | O | All `0`, 2.4 GHz `1`, 5 GHz `2` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Wi-Fi Country

Sets the Wi-Fi country code.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL10`, `SL10K`
*   **Parameters**:
    *   `countryCode` (string): ISO 3166-1 alpha-2 country code (e.g., "US", "KR").

```csharp
m3.SetWifiCountry("KR");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_country_code` |
| `value` | `String` | O | Two-letter country code |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Open Network Notification

Controls notifications for available open Wi-Fi networks.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.EnableOpenNetworkNotification();
m3.DisableOpenNetworkNotification();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_open_noti` |
| `value` | `Int` | O | Enable `1`, disable `0` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Roaming Configuration

Configures Wi-Fi roaming parameters.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL10`, `SL10K`

##### Set Roaming Trigger

Sets the signal strength (RSSI) threshold to start scanning for roaming.

*   **Index**:
    *   `0`: -80dBm
    *   `1`: -75dBm
    *   `2`: -70dBm
    *   `3`: -65dBm
    *   `4`: -60dBm

```csharp
m3.SetRoamingTrigger(index);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_roam_trigger` |
| `value` | `String` | O | SDK index encoded as a string |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


##### Set Roaming Delta

Sets the minimum signal difference required to roam to a new AP.

*   **Index**:
    *   `0`: 30dB
    *   `1`: 25dB
    *   `2`: 20dB
    *   `3`: 15dB
    *   `4`: 10dB
    *   `5`: 5dB
    *   `6`: 0dB

```csharp
m3.SetRoamingDelta(index);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_roam_delta` |
| `value` | `String` | O | SDK index encoded as a string |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Wi-Fi Sleep Policy

Controls when Wi-Fi should go to sleep.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.SetWifiSleepPolicyNever();         // Keep Wi-Fi on always
m3.SetWifiSleepPolicyPluggedOnly();   // Keep on when plugged in
m3.SetWifiSleepPolicyAlways();        // Allow sleep when screen is off
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_sleep` |
| `value` | `Int` | O | Never `0`, plugged only `1`, always `2` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Wi-Fi Stability

Optimizes Wi-Fi performance.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Note**: Not supported on Android 13 or later.

```csharp
m3.SetWifiStabilityNormal(); // Balanced
m3.SetWifiStabilityHigh();   // Performance focused (increases battery usage)
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_stability` |
| `value` | `Int` | O | Normal `1`, high `2` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Set Wi-Fi Channels

Sets the allowed Wi-Fi channels.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SM15`, `SL10`, `SL10K`
*   **Parameters**:
    *   `channels` (params int[]): List of channels to enable (e.g., 1, 6, 11, 36).

```csharp
m3.SetWifiChannel(1, 6, 11, 36);
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.config`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `wifi_channel` |
| `value` | `String[]` | O | Array of channel number strings |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Network Management

##### Set Access Point

Configures a Wi-Fi Access Point.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `accessPoint` (AccessPoint): The AccessPoint object to configure. Use `AccessPoint.CreateBuilder()` to create an instance.

```csharp
using M3Sdk.Xamarin.Startup;

var accessPoint = AccessPoint.CreateBuilder()
    .SetSsid("M3-WiFi")
    .SetSecurity("WPA2")
    .SetPassword("password")
    .SetHiddenSsid(false)
    .Build();

m3.SetAccessPoint(accessPoint);
```

`AccessPoint` can be created with its constructor or `AccessPoint.Builder`. Required properties are `Ssid` and `Security`. Optional values include `Password`, `EnableStatic`, `IpAddress`, `Mask`, `Gateway`, `Dns`, `MacRandom`, and `HiddenSsid`. Builder methods are `SetSsid`, `SetSecurity`, `SetPassword`, `SetEnableStatic`, `SetIpAddress`, `SetMask`, `SetGateway`, `SetDns`, `SetMacRandom`, `SetHiddenSsid`, and `Build`.

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `access_point` |
| `ssid` | `String` | O | SSID |
| `security` | `Int` | O | None `0`, WEP `1`, WPA/WPA2 PSK `2`, 802.1x EAP `3` |
| `password` | `String` | X | Password |
| `static_enable` | `Boolean` | X | Whether static IP is enabled |
| `ip_address` | `String` | X | IP address |
| `mask` | `String` | X | Subnet mask |
| `gateway` | `String` | X | Gateway |
| `dns` | `String` | X | DNS |
| `mac_random` | `Int` | X | Randomized MAC `0` (default), device MAC `1` |
| `hidden_ssid` | `Boolean` | X | Hidden SSID |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.

For direct broadcasts, `security` and `mac_random` use the StartUp receiver wire type, `Int`.
When omitted, `static_enable=false`, `mac_random=0`, and `hidden_ssid=false`; the other optional `String` extras are handled as `null`.


##### Clear Saved Wi-Fi Networks

Removes all saved Wi-Fi networks.

*   **Requires StartUp Version**: `6.4.11` or later

```csharp
m3.ClearSavedWifiNetworks();
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `remove_all_wifi` |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


##### Remove Wi-Fi Network

Removes a specific Wi-Fi network.

*   **Requires StartUp Version**: `6.4.11` or later
*   **Parameters**:
    *   `ssid` (string): The SSID of the network to remove.

```csharp
m3.RemoveWifiNetwork("M3-WiFi");
```

**Direct Broadcast**

*   **Action**: `com.android.server.startupservice.system`
*   **Target package**: Not set (implicit broadcast)

| Extra | Type | Required | Value |
|---|---|---|---|
| `setting` | `String` | O | `remove_wifi_by_ssid` |
| `ssid` | `String` | O | SSID to remove |

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Device Specific Wi-Fi Settings

These functions are available on specific devices.

##### Get Roaming Threshold

Retrieves the current Wi-Fi roaming threshold value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming threshold as a negative `int`.

```csharp
int roamingThreshold = m3.GetRoamingThreshold();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


##### Get Roaming Delta

Retrieves the current Wi-Fi roaming delta value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming delta as an `int`.

```csharp
int roamingDelta = m3.GetRoamingDelta();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


##### Get Wi-Fi Frequency Band

Retrieves the current preferred Wi-Fi frequency band value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: An `int` representing the frequency band:
    *   `0`: Automatic
    *   `1`: 2.4 GHz Only
    *   `2`: 5 GHz Only

```csharp
int frequencyBand = m3.GetWifiFrequencyBand();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


##### Get Wi-Fi Country Code

Retrieves the current Wi-Fi country code.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The country code as a `string`. Returns an empty string when none is configured.

```csharp
string countryCode = m3.GetWifiCountryCode();
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.
