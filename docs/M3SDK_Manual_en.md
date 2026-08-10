# M3 SDK Manual
Download PDF: [M3SDK_Manual_en_v2.3.12.pdf](https://github.com/m3mobile/M3SDK/releases/download/2.3.12/M3SDK_Manual_en_v2.3.12.pdf)


The M3 SDK provides a set of APIs to configure and control M3 Mobile devices.

## Table of Contents
- [Requirements](#requirements)
- [Installation](#installation)
  - [1. Add JitPack Repository](#1-add-jitpack-repository)
  - [2. Add Dependency](#2-add-dependency)
- [Basic Usage](#basic-usage)
  - [Accessing APIs](#accessing-apis)
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
  - [StartUp Setting API](#startup-setting-api)
    - [Reset StartUp Settings](#reset-startup-settings)
  - [KeyTool API](#keytool-api)
    - [Control Function-key Mode](#control-function-key-mode)
    - [Set Key Function](#set-key-function)
    - [Control Home and Recent Buttons](#control-home-and-recent-buttons)
    - [Control Scan-key Wake-Up](#control-scan-key-wake-up)
  - [AppCenter Kiosk API](#appcenter-kiosk-api)
    - [Change Kiosk Admin Password](#change-kiosk-admin-password)
    - [Keep Admin Mode While Screen Is Off](#keep-admin-mode-while-screen-is-off)
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

*   **Min SDK Version**: 24 (Android 7.0)
*   **Kotlin Version**: 1.8
*   **JDK Version**: 1.8

## Installation

### 1. Add JitPack Repository

Add the JitPack repository to your project's `settings.gradle` (or root `build.gradle`).

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") } // Add this line
    }
}
```

### 2. Add Dependency

Add the module dependency to your application's `build.gradle` file.

```kotlin
// Kotlin
dependencies {
    implementation("com.github.m3mobile:M3SDK:2.3.12")
}
```

```groovy
// Groovy
dependencies {
    implementation "com.github.m3mobile:M3SDK:2.3.12"
}
```

Please refer to the release notes for the latest version.
- https://github.com/m3mobile/M3SDK/releases

## Basic Usage

The SDK is automatically initialized when the application starts. You do not need to manually initialize it.

### Accessing APIs

All functions can be accessed through a singleton instance.

```kotlin
import net.m3mobile.sdk.M3Mobile

// Example: Turn on Airplane Mode
M3Mobile.instance.turnOnAirplaneMode()
```

### Strict Mode and Exception Handling

The M3 SDK provides a "Strict Mode" that influences how certain API calls behave when conditions (like device support or StartUp version) are not met.

**How it works:**

*   **Enabled**: When Strict Mode is enabled, API calls annotated with the following annotation will throw an exception if the condition is not met.
    *   `@SupportedModels`: Specifies the device models for which this API is available.
    *   `@UnsupportedModels`: Specifies the device models for which this API is NOT available.
    *   `@RequiresStartUp`: This indicates that a specific version or higher of StartUp must be installed in order to use this API.
    *   `@RequiresScanEmul`: This indicates that a specific version or higher of ScanEmul must be installed in order to use this API.

    The following exceptions may occur:
    *   `UnsupportedDeviceModelException`: Thrown if an API is called on a device model not listed as supported.
    *   `UnsatisfiedVersionException`: Thrown if an API requires a newer StartUp, ScanEmul, AppCenter, or KeyTool application version than what is installed on the device. For example, this occurs when a method requiring @RequiresStartUp(“2.0.0”) is called on a device with StartUp app 1.0.0 installed. AppCenter kiosk and `com.m3.keytoolsl20`-based KeyTool API version checks always run, regardless of Strict Mode.
    *   `KeyToolAppUnavailableException`: Thrown when the KeyTool companion app required by an API is not installed or is not visible. This availability check always runs because KeyTool requests are one-way broadcasts.

*   **Disabled**: In this mode, API calls that do not meet the required conditions (e.g., unsupported device, insufficient StartUp version) will **fail silently** and simply do nothing. No exceptions will be thrown, allowing your application to continue execution without interruption.

**Enabling Strict Mode:**

By default, Strict Mode is disabled.

To enable Strict Mode, add the following `<meta-data>` tag to your application's `AndroidManifest.xml` within the `<application>` tag:

```xml
<application ...>
    <meta-data
        android:name="M3_STRICT_MODE"
        android:value="true" />
</application>
```

It's recommended to enable Strict Mode during development and testing to catch potential issues early. For production environments, consider if silent failure or explicit exception handling is more suitable for your application's error strategy.


**Common Direct Broadcast Examples**

Use the action, target package, and typed extras shown in each API's `Direct Broadcast` table. In an MDM console such as AirWatch or SOTI, enter the same values in the corresponding broadcast fields.

```kotlin
// Implicit broadcast
val implicitRequest = Intent("ACTION_FROM_THIS_MANUAL")
    .putExtra("extra_key", "extra_value")
context.sendBroadcast(implicitRequest)

// Explicit broadcast
val explicitRequest = Intent("ACTION_FROM_THIS_MANUAL")
    .setPackage("TARGET_PACKAGE_FROM_THIS_MANUAL")
    .putExtra("extra_key", true)
context.sendBroadcast(explicitRequest)
```
---

## API

### Airplane Mode API

Controls the device's Airplane Mode.

#### Turn on Airplane Mode

Turns on airplane mode.

*   **Requires StartUp Version**: `6.3.7` or later

```kotlin
M3Mobile.instance.turnOnAirplaneMode()
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

```kotlin
M3Mobile.instance.turnOffAirplaneMode()
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
    *   `filePath` (String): The absolute path to the .apk file to install
    *   `allowSameVersionUpdate` (Boolean): Reinstalls when the APK `versionCode` matches the installed app
    *   `launchAfterInstall` (Boolean): Launches the installed app only after installation succeeds

```kotlin
M3Mobile.instance.installLocalApk(filePath)
M3Mobile.instance.installLocalApk(filePath, allowSameVersionUpdate = true)
M3Mobile.instance.installLocalApk(
    filePath,
    allowSameVersionUpdate = true,
    launchAfterInstall = true
)
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
    *   `url` (String): The URL of the APK file
    *   `allowSameVersionUpdate` (Boolean): Reinstalls when the APK `versionCode` matches the installed app
    *   `launchAfterInstall` (Boolean): Launches the installed app only after installation succeeds

```kotlin
M3Mobile.instance.installRemoteApk(url)
M3Mobile.instance.installRemoteApk(url, allowSameVersionUpdate = true)
M3Mobile.instance.installRemoteApk(
    url,
    allowSameVersionUpdate = true,
    launchAfterInstall = true
)
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
    *   `packageName` (String): The package name of the application to enable

```kotlin
M3Mobile.instance.enableApp(packageName: String)
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
    *   `packageName` (String): The package name of the application to disable

```kotlin
M3Mobile.instance.disableApp(packageName: String)
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
    *   `packageName` (String): The package name of the application to enable and run
*   **Notes**:
    *   This API automatically enables the package before launching it. You do not need to call `enableApp` first.
    *   `enableApp` only enables the package and does not launch it.

```kotlin
M3Mobile.instance.runApp(packageName: String)
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
    *   `packageName` (String): The package name of the application to enable, run, and pin
*   **Prerequisites**:
    *   Screen Pinning must be enabled in the OS/StartUp environment.
*   **Notes**:
    *   This API automatically enables the package before launching and pinning it. You do not need to call `enableApp` first.
    *   StartUp 6.8.0 does not provide an SDK or broadcast API to stop app pinning. To exit the pinned app manually, press the Home button 10 times in a row.

```kotlin
M3Mobile.instance.runAndPinApp(packageName: String)
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
    *   `value` (Int): The desired volume level.

```kotlin
M3Mobile.instance.setMediaVolume(value: Int)
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
    *   `value` (Int): The desired volume level.

```kotlin
M3Mobile.instance.setRingtoneVolume(value: Int)
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
*   **Requires Android Version**: Android 14 (UpsideDownCake) or later
*   **Range**: Same as `setRingtoneVolume`
*   **Parameters**:
    *   `value` (Int): The desired volume level.

```kotlin
M3Mobile.instance.setNotificationVolume(value: Int)
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
    *   `value` (Int): The desired volume level.

```kotlin
M3Mobile.instance.setAlarmVolume(value: Int)
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

```kotlin
M3Mobile.instance.enableVibrationMode()
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

```kotlin
M3Mobile.instance.disableVibrationMode()
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

```kotlin
M3Mobile.instance.setDisplaySetting(displaySetting: DisplaySetting)
```

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

```kotlin
// Coroutine (for kotlin)
M3Mobile.instance.getSerialNumber(): String

// Callback (for java)
M3Mobile.instance.getSerialNumber(callback: RequestCallback<String>): Job
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

Locks the expansion of the status bar. When locked, the user cannot pull down the status bar.

*   **Requires StartUp Version**: `6.4.12` or later

```kotlin
M3Mobile.instance.lockStatusBarExpansion()
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

Unlocks the expansion of the status bar.

*   **Requires StartUp Version**: `6.4.12` or later

```kotlin
M3Mobile.instance.unlockStatusBarExpansion()
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

```kotlin
// Coroutine (for kotlin)
M3Mobile.instance.getBluetoothMac(): String

// Callback (for java)
M3Mobile.instance.getBluetoothMac(callback: RequestCallback<String>): Job
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
    *   `language` (String): The language code (e.g., "en").
    *   `country` (String): The country code (e.g., "US").

```kotlin
M3Mobile.instance.setLanguage(language: String, country: String)
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
    *   `apn` (Apn): The `Apn` object containing configuration details. Use `Apn.builder()` to create an instance.

```kotlin
M3Mobile.instance.setApn(apn: Apn)
```

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

```kotlin
M3Mobile.instance.enableNfc()
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

```kotlin
M3Mobile.instance.disableNfc()
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
    *   `packageName` (String): The package name of the application.
    *   `permission` (String): The fully qualified name of the permission (e.g., `android.permission.CAMERA`).

```kotlin
M3Mobile.instance.grantPermission(packageName: String, permission: String)
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
    *   `packageName` (String): The package name of the application.
    *   `permission` (String): The fully qualified name of the permission to revoke.

```kotlin
M3Mobile.instance.revokePermission(packageName: String, permission: String)
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
*   **Supported Model**: `SM24` currently returns `SUCCESS`; other models return `UNSUPPORTED_DEVICE`.
*   **Parameter**: `packageName` is the exact package name of an already installed application.
*   **When to call**: Call when the customer application starts, before starting or connecting the screen-capture application. Repeated calls are safe.

```kotlin
val result = M3Mobile.instance.allowProjectMedia("net.christianbeier.droidvnc_ng")
when (result.status) {
    ProjectMediaStatus.SUCCESS -> Log.i("ProjectMedia", "PROJECT_MEDIA allowed")
    else -> Log.e("ProjectMedia", "${result.status}: ${result.errorMessage}")
}
```

A callback overload is also available and returns a cancellable `Job`:

```kotlin
val request = M3Mobile.instance.allowProjectMedia(packageName) { result, error ->
    when {
        error != null -> Log.e("ProjectMedia", "StartUp transport failure", error)
        result != null -> Log.i("ProjectMedia", "${result.status} (${result.status.code})")
    }
}
```

`ProjectMediaResult` represents a StartUp feature outcome. A thrown exception or callback `error`
represents a transport failure, such as a broadcast failure or response timeout, and has no
`ProjectMediaStatus`. An unrecognized response code is normalized to `APPLY_FAILED`, with the raw
code included in `errorMessage`.

| Code | `ProjectMediaStatus` | Meaning | Recommended action |
|---:|---|---|---|
| 0 | `SUCCESS` | StartUp allowed the operation and verified `MODE_ALLOWED`. | Start or connect the screen-capture application. |
| 1 | `UNSUPPORTED_DEVICE` | The current model does not support the feature. | Use SM24 or a StartUp build that explicitly supports the model. |
| 2 | `TARGET_NOT_INSTALLED` | The requested package is not installed. | Install the application and retry with its exact package name. |
| 3 | `INVALID_TARGET` | The package name is blank, or the package and UID do not match. | Check the package name and resolved UID. |
| 4 | `PERMISSION_DENIED` | StartUp cannot control AppOps with its current system privileges. | Check firmware signing, shared UID, and AppOps privileges. |
| 5 | `APP_OP_UNAVAILABLE` | The required AppOps API is unavailable. | Check Android framework and StartUp compatibility. |
| 6 | `APPLY_FAILED` | Applying the mode failed, or readback was not `MODE_ALLOWED`. | Inspect StartUp logs and the package, UID, and AppOps state. |

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

---

### Quick Tile API

Customizes the Quick Settings tiles in the System UI.

#### Set Quick Tiles

Sets the Quick Tiles to be displayed.

*   **Requires StartUp Version**: `6.4.1` or later
*   **Parameters**:
    *   `quickTile` (vararg QuickTile): One or more `QuickTile` objects to add.

```kotlin
M3Mobile.instance.setQuickTiles(vararg quickTile: QuickTile)
```

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

```kotlin
M3Mobile.instance.resetQuickTile()
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

```kotlin
M3Mobile.instance.startScan()
```

**Direct Broadcast**

*   **Action**: `android.intent.action.M3SCANNER_BUTTON_DOWN`
*   **Target package**: Not set (implicit broadcast)
*   **Extra**: None

> This is a one-way request. Sending the broadcast does not guarantee that the setting was applied. Verify the resulting state separately in the MDM.


#### Stop Scan

Stops the scanning process.

*   **Requires ScanEmul Version**: `2.13.0` or later

```kotlin
M3Mobile.instance.stopScan()
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
    *   `4`: Succeed to open
    *   `8`: Succeed to close

```kotlin
// Coroutine
M3Mobile.instance.getScannerStatus(): Int

// Callback
M3Mobile.instance.getScannerStatus(callback: RequestCallback<Int>): Job
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

Retrieves the type of the scanner hardware.

*   **Requires ScanEmul Version**: `2.13.0` or later
*   **Returns**: The scanner type string.

```kotlin
// Coroutine
M3Mobile.instance.getScannerType(): String

// Callback
M3Mobile.instance.getScannerType(callback: RequestCallback<String>): Job
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
    *   `listener` (OnScanResultListener): The listener object to receive scan results.

```kotlin
// Register
M3Mobile.instance.registerOnScanResultListener(listener: OnScanResultListener)

// Unregister
M3Mobile.instance.unregisterOnScanResultListener(listener: OnScanResultListener)
```

**Direct Broadcast**

No direct command broadcast is available. Results use the ScanEmul message connection, so the SDK or a receiving application is required.


#### GS1 Parsed Listener

Registers or unregisters a listener to receive GS1 parsed scan results.

*   **Requires ScanEmul Version**: `4.11.0` or later
*   **Parameters**:
    *   `listener` (OnGS1ParsedListener): The listener object to receive GS1 parsed results.

```kotlin
// Register
M3Mobile.instance.registerOnGS1ParsedListener(listener: OnGS1ParsedListener)

// Unregister
M3Mobile.instance.unregisterOnGS1ParsedListener(listener: OnGS1ParsedListener)
```

**Direct Broadcast**

No direct command broadcast is available. Results use the ScanEmul message connection, so the SDK or a receiving application is required.


#### Digital Link Parsed Listener

Registers or unregisters a listener to receive Digital Link parsed scan results.

*   **Requires ScanEmul Version**: `4.11.0` or later
*   **Parameters**:
    *   `listener` (OnDigitalLinkParsedListener): The listener object to receive Digital Link parsed results.

```kotlin
// Register
M3Mobile.instance.registerOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener)

// Unregister
M3Mobile.instance.unregisterOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener)
```

**Direct Broadcast**

No direct command broadcast is available. Results use the ScanEmul message connection, so the SDK or a receiving application is required.


#### Scanner Settings

Configures various scanner options. These settings apply to the currently active profile.

*   **Requires ScanEmul Version**: `2.11.0` or later

##### Feedback

```kotlin
// Sound
M3Mobile.instance.setScanSound(ScanSound.BEEP) 
// Enum: NONE, BEEP, DING_DONG

// Vibration
M3Mobile.instance.enableScanVibration()
M3Mobile.instance.disableScanVibration()

// LED
M3Mobile.instance.enableScanLed()
M3Mobile.instance.disableScanLed()
M3Mobile.instance.setScanLedTime(timeMillis: Int) // Range: 1 to 1000
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

```kotlin
M3Mobile.instance.setScannerReadMode(ReadMode.MULTIPLE)
// Enum: AIMING_AND_RELEASE, ASYNC, CONTINUE, MULTIPLE, PRESENTATION, SYNC

// Getter
M3Mobile.instance.getScannerReadMode()
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

```kotlin
// Output Mode
M3Mobile.instance.setScanResultOutputMode(OutputMode.COPY_AND_PASTE)
// Enum: COMMIT_TEXT, COPY_AND_PASTE, COPY_TO_CLIPBOARD, KEY_EMULATION

// Formatting
M3Mobile.instance.setScanResultPrefix("Prefix")
M3Mobile.instance.setScanResultPostfix("Postfix")
M3Mobile.instance.setScanResultEndCharacter(EndCharacter.ENTER)
// Enum: ENTER, KEYBOARD_ENTER, KEYBOARD_SPACE, KEYBOARD_TAB, NONE, SPACE, TAB

// Getters
M3Mobile.instance.getScanResultOutputMode()
M3Mobile.instance.getScanResultPrefix()
M3Mobile.instance.getScanResultPostfix()
M3Mobile.instance.getScanResultEndCharacter()
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

```kotlin
M3Mobile.instance.isScannerProfileEnabled()
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

```kotlin
val options = ScannerButtonUiOptions(
    imagePath = "/sdcard/Download/ScanEmul_Floating_Button_Images/target.png",
    opacityPercent = 80,
    size = ScannerButtonUiSize.LARGE,
)

val result = M3Mobile.instance.setAndVerifyScannerButtonUi(options)
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

```kotlin
val requestId = UUID.randomUUID().toString()
val receiver = object : BroadcastReceiver() {
    override fun onReceive(receiverContext: Context, intent: Intent) {
        if (intent.getStringExtra("setting") != "scanner_button_ui") return
        if (intent.getStringExtra("request_id") != requestId) return

        val success = intent.getBooleanExtra("success", false)
        val status = intent.getStringExtra("status")
        val runtimeApplied = intent.getBooleanExtra("runtime_applied", false)
        receiverContext.unregisterReceiver(this)
    }
}

val filter = IntentFilter("com.android.server.scannerservice.setting")
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
} else {
    context.registerReceiver(receiver, filter)
}

val request = Intent("com.android.server.scannerservice.getsetting")
    .setPackage("net.m3mobile.app.scanemul")
    .putExtra("setting", "scanner_button_ui")
    .putExtra("request_id", requestId)
context.sendOrderedBroadcast(request, null)

// Unregister the receiver from timeout handling when no response arrives.
```


---

### StartUp Setting API

Manages the StartUp SDK's own settings.

#### Reset StartUp Settings

Resets the StartUp settings to their default values.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
M3Mobile.instance.resetStartUpSetting()
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
### KeyTool API

Controls physical key configuration through the KeyTool companion apps. KeyTool methods are exposed
directly from `M3Mobile.instance`, like the StartUp and ScanEmul methods.

> **One-way request:** KeyTool broadcasts do not return an acknowledgement. A method returning
> normally means only that Android accepted the request. It does not prove that the device setting
> changed. Verify the physical key after the call. If the required package is unavailable, the SDK
> throws `KeyToolAppUnavailableException` with the method and package details. APIs based on
> `com.m3.keytoolsl20` also verify the minimum version regardless of Strict Mode. An older version
> causes `UnsatisfiedVersionException` with method, model, package, current, and required versions.

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

Enables, disables, or locks Function-key mode.

*   **Supported model**: `SL20K`
*   **Required package**: `com.m3.keytoolsl20` version `1.2.6` or later

```kotlin
M3Mobile.instance.enableFN()
M3Mobile.instance.disableFN()
M3Mobile.instance.lockFN()
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
*   **Parameters**:
    *   `key`: KeyTool key title.
    *   `function`: KeyTool function title.

```kotlin
try {
    M3Mobile.instance.setKeyFunction(
        key = "Left Scan",
        function = "Volume Up"
    )
    // REQUEST_SENT_UNVERIFIED: verify the physical key on the device.
} catch (error: Exception) {
    Log.e("M3SDK", "KeyTool request failed", error)
}
```

Use the current KeyTool title spelling, including `Volume Up` and `Volume Down`. KeyTool 1.4.1
also normalizes settings saved by older releases as `Volume up` or `Volume down`.

On SM24, the three-argument overload includes the key mapping and Wake-Up state in one
`ACTION_SET_KEY` request.

```kotlin
M3Mobile.instance.setKeyFunction(
    key = "Left Scan",
    function = "Scan",
    wakeUpEnabled = true,
)
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

Enables or disables the SystemUI Home and Recent navigation buttons. These methods send the same
`ACTION_SET_KEY` request used by KeyTool 1.4.1 with `Default` for enabled and `Disable` for disabled.

*   **Supported models**: `SM24`, `SM25`
*   **Required package**: `com.m3.keytoolsl20` version `1.4.1` or later

```kotlin
M3Mobile.instance.enableHomeButton()
M3Mobile.instance.disableHomeButton()
M3Mobile.instance.enableRecentButton()
M3Mobile.instance.disableRecentButton()
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

```kotlin
M3Mobile.instance.enableLeftScanWakeUp()
M3Mobile.instance.disableLeftScanWakeUp()
M3Mobile.instance.enableRightScanWakeUp()
M3Mobile.instance.disableRightScanWakeUp()
```

The published-package sample displays the installed KeyTool package version and reports one-way
calls as `REQUEST_SENT_UNVERIFIED` rather than success.

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
exposed directly from `M3Mobile.instance`.

> **One-way request:** AppCenter broadcasts do not return an acknowledgement. A normal return means
> only that Android accepted the request. It does not prove that AppCenter applied the setting.
> AppCenter `2.2.0` or later must be installed and able to receive broadcasts. The SDK verifies
> AppCenter availability and version regardless of Strict Mode.

#### Change Kiosk Admin Password

Requests an AppCenter kiosk administrator password change.

*   **Requires AppCenter Version**: `2.2.0` or later
*   **Parameters**:
    *   `currentPassword`: Current administrator password. Empty strings are rejected.
    *   `newPassword`: New administrator password. Length must be 4 to 20 characters.

The SDK does not trim either password. If the current password is wrong, AppCenter may ignore the
request and the SDK cannot confirm the result.

```kotlin
M3Mobile.instance.changeKioskAdminPassword(currentPassword, newPassword)
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

Sets whether AppCenter keeps administrator mode when the screen turns off.

*   **Requires AppCenter Version**: `2.2.0` or later
*   **Parameters**:
    *   `enabled`: `true` keeps administrator mode after screen off. `false` restores the normal
        user-mode behavior and may require administrator login again.

Administrator mode is not preserved after reboot.

```kotlin
M3Mobile.instance.setKeepAdminModeOnSleep(true)
M3Mobile.instance.setKeepAdminModeOnSleep(false)
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


### Time API

Configures system time, timezone, and NTP server settings.

#### Set Date and Time

Sets the date and time of the device.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `dateTime` (java.time.LocalDateTime): The date and time to set.

```kotlin
M3Mobile.instance.setDateTime(dateTime: LocalDateTime)
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
    *   `host` (String): The hostname or IP address of the NTP server.

```kotlin
M3Mobile.instance.setNtpServer(host: String)
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
    *   `timezone` (String): The timezone identifier (e.g., "America/New_York").

```kotlin
M3Mobile.instance.setTimeZone(timezone: String)
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
*   **Returns**: The NTP server address as a String.

```kotlin
M3Mobile.instance.getNtpServer(): String
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


#### Get NTP Interval

Retrieves the currently configured NTP synchronization interval.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP synchronization interval in milliseconds (Int).

```kotlin
M3Mobile.instance.getNtpInterval(): Int
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


#### Get Timezone

Retrieves the system's current default timezone.

*   **Returns**: The timezone identifier as a String.

```kotlin
M3Mobile.instance.getTimeZone(): String
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

```kotlin
M3Mobile.instance.setUsbModeMtp()
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

```kotlin
M3Mobile.instance.setUsbModeRndis()
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

```kotlin
M3Mobile.instance.setUsbModeMidi()
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

```kotlin
M3Mobile.instance.setUsbModePtp()
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

```kotlin
M3Mobile.instance.setUsbModeNone()
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

```kotlin
M3Mobile.instance.getCurrentUsbModes(): List<String>
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

```kotlin
// Coroutine (for kotlin)
M3Mobile.instance.getWifiMac(): String

// Callback (for java)
M3Mobile.instance.getWifiMac(callback: RequestCallback<String>): Job
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

Retrieves the factory Wi-Fi MAC address of the device. This is different from `getWifiMac()`, which can return the current or randomized Wi-Fi MAC address depending on Android state and Wi-Fi connection history.

The device does not need to be connected to a Wi-Fi AP, but Wi-Fi must be turned on. If Wi-Fi is turned off, StartUp may not be able to return the factory Wi-Fi MAC address.

*   **Requires StartUp Version**: `6.7.3` or later
*   **Returns**: `FactoryWifiMacResult`

```kotlin
// Coroutine (for kotlin)
val result = M3Mobile.instance.getFactoryWifiMac()
if (result.success) {
    val mac = result.macAddress
} else {
    val error = result.errorMessage
}
```

```java
// Callback (for java)
M3Mobile.INSTANCE.getInstance().getFactoryWifiMac((result, error) -> {
    if (error != null) {
        return;
    }

    if (result.isSuccess()) {
        String mac = result.getMacAddress();
    } else {
        String message = result.getErrorMessage();
    }
});
```

Direct StartUp broadcast response:

```java
BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        String mac = intent.getStringExtra("get_factory_wifi_mac");
        boolean success = intent.getBooleanExtra("get_factory_wifi_mac_success", false);
        String error = intent.getStringExtra("get_factory_wifi_mac_error_message");
    }
};
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
    *   `enabled` (Boolean): `true` to enable Wi-Fi, `false` to disable Wi-Fi.

```kotlin
M3Mobile.instance.setWifiEnabled(true)
M3Mobile.instance.setWifiEnabled(false)
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

```kotlin
M3Mobile.instance.enableCaptivePortalDetection()
M3Mobile.instance.disableCaptivePortalDetection()
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

```kotlin
M3Mobile.instance.allowAllWifiFrequencyBand()
M3Mobile.instance.allowOnly2_4GHzWifiFrequencyBand()
M3Mobile.instance.allowOnly5GHzWifiFrequencyBand()
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
    *   `countryCode` (String): ISO 3166-1 alpha-2 country code (e.g., "US", "KR").

```kotlin
M3Mobile.instance.setWifiCountry(countryCode: String)
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

```kotlin
M3Mobile.instance.enableOpenNetworkNotification()
M3Mobile.instance.disableOpenNetworkNotification()
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

```kotlin
M3Mobile.instance.setRoamingTrigger(index: Int)
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

```kotlin
M3Mobile.instance.setRoamingDelta(index: Int)
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

```kotlin
M3Mobile.instance.setWifiSleepPolicyNever()         // Keep Wi-Fi on always
M3Mobile.instance.setWifiSleepPolicyPluggedOnly()   // Keep on when plugged in
M3Mobile.instance.setWifiSleepPolicyAlways()        // Allow sleep when screen is off
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

```kotlin
M3Mobile.instance.setWifiStabilityNormal() // Balanced
M3Mobile.instance.setWifiStabilityHigh()   // Performance focused (High battery usage)
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
    *   `channels` (vararg Int): List of channels to enable (e.g., 1, 6, 11, 36).

```kotlin
M3Mobile.instance.setWifiChannel(vararg channels: Int)
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
    *   `accessPoint` (AccessPoint): The AccessPoint object to configure. Use `AccessPoint.builder()` to create an instance.

```kotlin
M3Mobile.instance.setAccessPoint(accessPoint: AccessPoint)
```

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

```kotlin
M3Mobile.instance.clearSavedWifiNetworks()
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
    *   `ssid` (String): The SSID of the network to remove.

```kotlin
M3Mobile.instance.removeWifiNetwork(ssid: String)
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
*   **Returns**: The roaming threshold as a negative `Int`.

```kotlin
M3Mobile.instance.getRoamingThreshold(): Int
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


##### Get Roaming Delta

Retrieves the current Wi-Fi roaming delta value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming delta as an `Int`.

```kotlin
M3Mobile.instance.getRoamingDelta(): Int
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


##### Get Wi-Fi Frequency Band

Retrieves the current preferred Wi-Fi frequency band value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: An `Int` representing the frequency band:
    *   `0`: Automatic
    *   `1`: 5 GHz Only
    *   `2`: 2.4 GHz Only

```kotlin
M3Mobile.instance.getWifiFrequencyBand(): Int
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.


##### Get Wi-Fi Country Code

Retrieves the current Wi-Fi country code.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The country code as a `String`.

```kotlin
M3Mobile.instance.getWifiCountryCode(): String
```

**Direct Broadcast**

No direct command broadcast is available. This API reads Android system settings or a sticky system broadcast and does not send a command broadcast.
