# M3 SDK Xamarin Manual

NuGet package: [M3Mobile.M3Sdk.Xamarin 2.3.6](https://www.nuget.org/packages/M3Mobile.M3Sdk.Xamarin/2.3.6)


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
Install-Package M3Mobile.M3Sdk.Xamarin -Version 2.3.6
```

The NuGet package page is linked at the top of this document.

### 2. Verify Package Reference

The project file should contain the following package reference.

```xml
<PackageReference Include="M3Mobile.M3Sdk.Xamarin" Version="2.3.6" />
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

string ntpServer = m3.Time.GetNtpServer();
IList<string> usbModes = m3.Usb.GetCurrentUsbModes();
int roamingDelta = m3.Wifi.GetRoamingDelta();
```

`IM3Sdk` exposes `StartUp`, `ScanEmul`, `Time`, `Wifi`, and `Usb` groups. Their types are `IStartUpApi`, `IScanEmulApi`, `ITimeApi`, `IWifiApi`, and `IUsbApi`. `M3Mobile.Create(...)` returns the public root implementation `M3Sdk` as `IM3Sdk`, and the group implementations are `StartUpApi`, `ScanEmulApi`, `TimeApi`, `WifiApi`, and `UsbApi`. The `ScanEmul` group implements `IDisposable` to clean up internal scan connections. Calling `IM3Sdk.Dispose()` cleans it up together.

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
    *   `UnsatisfiedVersionException`: Thrown if the installed StartUp or ScanEmul app version is lower than the API requirement.

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

#### Turn off Airplane Mode

Turns off airplane mode.

*   **Requires StartUp Version**: `6.3.7` or later

```csharp
m3.TurnOffAirplaneMode();
```

---

### App API

Installs applications and enables or disables specific packages.

#### Install Local APK

Installs an APK from a local file path.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `filePath` (string): The absolute path to the .apk file to install.

```csharp
m3.InstallLocalApk(filePath);
```

#### Install Remote APK

Installs an APK from a remote URL.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `url` (string): The URL of the APK file.

```csharp
m3.InstallRemoteApk(url);
```

#### Enable Application

Enables a specified application package.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application to enable.

```csharp
m3.EnableApp(packageName);
```

#### Disable Application

Disables a specified application package.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application to disable.

```csharp
m3.DisableApp(packageName);
```

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

#### Set Notification Volume

Sets the notification volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**: Same as `SetRingtoneVolume`
*   **Parameters**:
    *   `value` (int): The desired volume level.

```csharp
m3.SetNotificationVolume(value);
```

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

#### Enable Vibration Mode

Enables vibration mode. This sets ringtone and notification volumes to 0.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.EnableVibrationMode();
```

#### Disable Vibration Mode

Disables vibration mode.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.DisableVibrationMode();
```

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

#### Lock Status Bar Expansion

Locks status bar expansion so the user cannot pull down notifications or quick settings.

*   **Requires StartUp Version**: `6.4.12` or later

```csharp
m3.LockStatusBarExpansion();
```

#### Unlock Status Bar Expansion

Unlocks status bar expansion.

*   **Requires StartUp Version**: `6.4.12` or later

```csharp
m3.UnlockStatusBarExpansion();
```

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

#### Enable NFC

Enables Near Field Communication (NFC).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Requires Android Version**: Android 11 (R) or later

```csharp
m3.EnableNfc();
```

#### Disable NFC

Disables Near Field Communication (NFC).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Requires Android Version**: Android 11 (R) or later

```csharp
m3.DisableNfc();
```

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

#### Revoke Permission

Revokes a specific runtime permission from a target application package.

*   **Requires StartUp Version**: `6.4.17` or later
*   **Parameters**:
    *   `packageName` (string): The package name of the application.
    *   `permission` (string): The fully qualified name of the permission to revoke.

```csharp
m3.RevokePermission("com.example.app", "android.permission.CAMERA");
```

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

#### Reset Quick Tiles

Resets the Quick Tiles configuration to the default state.

*   **Requires StartUp Version**: `6.4.1` or later

```csharp
m3.ResetQuickTile();
```

---

### Scanner API

Controls the barcode scanner and configures scanning preferences.

#### Start Scan

Starts the scanning process.

*   **Requires ScanEmul Version**: `2.13.0` or later

```csharp
m3.StartScan();
```

#### Stop Scan

Stops the scanning process.

*   **Requires ScanEmul Version**: `2.13.0` or later

```csharp
m3.StopScan();
```

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

---

### StartUp Setting API

Manages the StartUp SDK's own settings.

#### Reset StartUp Settings

Resets the StartUp settings to their default values.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.ResetStartUpSetting();
```

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

#### Set NTP Server

Sets the NTP server for automatic time synchronization. This setting takes effect after the next reboot.

*   **Requires StartUp Version**: `6.4.9` or later
*   **Parameters**:
    *   `host` (string): The hostname or IP address of the NTP server.

```csharp
m3.SetNtpServer("time.android.com");
```

#### Set Timezone

Sets the system's default timezone.

*   **Requires StartUp Version**: `6.5.9` or later
*   **Parameters**:
    *   `timezone` (string): The timezone identifier (e.g., "Asia/Seoul", "America/New_York").

```csharp
m3.SetTimeZone("Asia/Seoul");
```

#### Get NTP Server

Retrieves the currently configured NTP server address.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP server address string.

```csharp
string ntpServer = m3.GetNtpServer();
```

#### Get NTP Interval

Retrieves the currently configured NTP synchronization interval.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP synchronization interval in milliseconds (int).

```csharp
int ntpInterval = m3.GetNtpInterval();
```

#### Get Timezone

Retrieves the system's current default timezone.

*   **Returns**: The timezone identifier string.

```csharp
string timeZone = m3.GetTimeZone();
```

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

#### Set USB Mode to RNDIS

Sets the USB connection mode to RNDIS (USB Tethering).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeRndis();
```

#### Set USB Mode to MIDI

Sets the USB connection mode to MIDI.

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeMidi();
```

#### Set USB Mode to PTP

Sets the USB connection mode to PTP (Picture Transfer Protocol).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModePtp();
```

#### Disable USB Data (Charging Only)

Disables all USB data connections, setting the mode to charging only.

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```csharp
m3.SetUsbModeNone();
```

#### Get Current USB Modes

Retrieves the current USB connection mode.

*   **Returns**: A list of strings representing the currently active USB modes. Returns an empty list if no active modes are found.

```csharp
using System.Collections.Generic;

IList<string> usbModes = m3.GetCurrentUsbModes();
```

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

You can also call StartUp directly with broadcasts when you do not use M3SDK.

```csharp
Intent request = new Intent("com.android.server.startupservice.system");
request.PutExtra("setting", "get_factory_wifi_mac");
context.SendBroadcast(request);

BroadcastReceiver receiver = new FactoryWifiMacReceiver();

public sealed class FactoryWifiMacReceiver : BroadcastReceiver
{
    public override void OnReceive(Context context, Intent intent)
    {
        string mac = intent.GetStringExtra("get_factory_wifi_mac") ?? string.Empty;
        bool success = intent.GetBooleanExtra("get_factory_wifi_mac_success", false);
        string error = intent.GetStringExtra("get_factory_wifi_mac_error_message") ?? string.Empty;
    }
}
```

#### Captive Portal Detection

Controls whether the device detects captive portals (login pages for public Wi-Fi).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL20`

```csharp
m3.EnableCaptivePortalDetection();
m3.DisableCaptivePortalDetection();
```

#### Frequency Band Control

Restricts the Wi-Fi frequency band usage.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SM15`, `SL10`, `SL10K`

```csharp
m3.AllowAllWifiFrequencyBand();          // Allow all bands
m3.AllowOnly2_4GHzWifiFrequencyBand();   // Allow only the 2.4 GHz band
m3.AllowOnly5GHzWifiFrequencyBand();     // Allow only the 5 GHz band
```

#### Set Wi-Fi Country

Sets the Wi-Fi country code.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL10`, `SL10K`
*   **Parameters**:
    *   `countryCode` (string): ISO 3166-1 alpha-2 country code (e.g., "US", "KR").

```csharp
m3.SetWifiCountry("KR");
```

#### Open Network Notification

Controls notifications for available open Wi-Fi networks.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.EnableOpenNetworkNotification();
m3.DisableOpenNetworkNotification();
```

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

#### Wi-Fi Sleep Policy

Controls when Wi-Fi should go to sleep.

*   **Requires StartUp Version**: `6.2.14` or later

```csharp
m3.SetWifiSleepPolicyNever();         // Keep Wi-Fi on always
m3.SetWifiSleepPolicyPluggedOnly();   // Keep on when plugged in
m3.SetWifiSleepPolicyAlways();        // Allow sleep when screen is off
```

#### Wi-Fi Stability

Optimizes Wi-Fi performance.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Note**: Not supported on Android 13 or later.

```csharp
m3.SetWifiStabilityNormal(); // Balanced
m3.SetWifiStabilityHigh();   // Performance focused (increases battery usage)
```

#### Set Wi-Fi Channels

Sets the allowed Wi-Fi channels.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SM15`, `SL10`, `SL10K`
*   **Parameters**:
    *   `channels` (params int[]): List of channels to enable (e.g., 1, 6, 11, 36).

```csharp
m3.SetWifiChannel(1, 6, 11, 36);
```

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

##### Clear Saved Wi-Fi Networks

Removes all saved Wi-Fi networks.

*   **Requires StartUp Version**: `6.4.11` or later

```csharp
m3.ClearSavedWifiNetworks();
```

##### Remove Wi-Fi Network

Removes a specific Wi-Fi network.

*   **Requires StartUp Version**: `6.4.11` or later
*   **Parameters**:
    *   `ssid` (string): The SSID of the network to remove.

```csharp
m3.RemoveWifiNetwork("M3-WiFi");
```

#### Device Specific Wi-Fi Settings

These functions are available on specific devices.

##### Get Roaming Threshold

Retrieves the current Wi-Fi roaming threshold value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming threshold as a negative `int`.

```csharp
int roamingThreshold = m3.GetRoamingThreshold();
```

##### Get Roaming Delta

Retrieves the current Wi-Fi roaming delta value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming delta as an `int`.

```csharp
int roamingDelta = m3.GetRoamingDelta();
```

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

##### Get Wi-Fi Country Code

Retrieves the current Wi-Fi country code.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The country code as a `string`. Returns an empty string when none is configured.

```csharp
string countryCode = m3.GetWifiCountryCode();
```
