# M3 SDK Manual
Download PDF: [M3SDK_Manual_en.pdf](https://github.com/user-attachments/files/24117890/M3SDK_Manual_en.pdf)

The M3 SDK provides a set of APIs to configure and control M3 Mobile devices.

## Table of Contents
- [Requirements](#requirements)
- [Installation](#installation)
  - [1. Add JitPack Repository](#1-add-jitpack-repository)
  - [2. Add Dependency](#2-add-dependency)
- [Basic Usage](#basic-usage)
  - [Accessing APIs](#accessing-apis)
  - [Strict Mode and Exception Handling](#strict-mode-and-exception-handling)
- [StartUp](#startup-api)
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
  - [Network API](#network-api)
    - [Set APN](#set-apn)
  - [Permission API](#permission-api)
    - [Grant Permission](#grant-permission)
    - [Revoke Permission](#revoke-permission)
  - [Quick Tile API](#quick-tile-api)
    - [Set Quick Tiles](#set-quick-tiles)
    - [Reset Quick Tiles](#reset-quick-tiles)
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
    // Replace <TAG> with the specific version you want to use (e.g., 1.0.0)
    implementation("com.github.m3mobile.M3SDK:startup:<TAG>")
}
```

```groovy
// Groovy
dependencies {
    implementation "com.github.m3mobile.M3SDK:startup:<TAG>"
}
```

The latest version is as follows:
- StartUp SDK : 1.0.0

## Basic Usage

The SDK is automatically initialized when the application starts. You do not need to manually initialize it.

### Accessing APIs

All functions can be accessed through a singleton instance.

```kotlin
import net.m3mobile.sdk.startup.M3StartUp

// Example: Turn on Airplane Mode
M3StartUp.instance.turnOnAirplaneMode()
```

### Strict Mode and Exception Handling

The M3 SDK provides a "Strict Mode" that influences how certain API calls behave when conditions (like device support or StartUp version) are not met.

**How it works:**

*   **Enabled**: When Strict Mode is enabled, API calls annotated with the following annotation will throw an exception if the condition is not met.
    *   `@SupportedModels`: Specifies the device models for which this API is available.
    *   `@UnsupportedModels`: Specifies the device models for which this API is NOT available.
    *   `@RequiresStartUp`: This indicates that a specific version or higher of StartUp must be installed in order to use this API.

    The following exceptions may occur:
    *   `UnsupportedDeviceModelException`: Thrown if an API is called on a device model not listed as supported.
    *   `UnsatisfiedVersionException`: Thrown if an API requires a newer StartUp application version than what is installed on the device. For example, this occurs when a method requiring @RequiresStartUp(“2.0.0”) is called on a device with StartUp app 1.0.0 installed.
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

---

## StartUp API

### Airplane Mode API

Controls the device's Airplane Mode.

#### Turn on Airplane Mode

Turns on airplane mode.

*   **Requires StartUp Version**: `6.3.7` or later

```kotlin
fun turnOnAirplaneMode()
```

#### Turn off Airplane Mode

Turns off airplane mode.

*   **Requires StartUp Version**: `6.3.7` or later

```kotlin
fun turnOffAirplaneMode()
```

---

### App API

Installs applications and enables or disables specific packages.

#### Install Local APK

Installs an APK from a local file path.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `filePath` (String): The absolute path to the .apk file to install

```kotlin
fun installLocalApk(filePath: String)
```

#### Install Remote APK

Installs an APK from a remote URL.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `url` (String): The URL of the APK file

```kotlin
fun installRemoteApk(url: String)
```

#### Enable Application

Enables a specified application package.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `packageName` (String): The package name of the application to enable

```kotlin
fun enableApp(packageName: String)
```

#### Disable Application

Disables a specified application package.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `packageName` (String): The package name of the application to disable

```kotlin
fun disableApp(packageName: String)
```

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
fun setMediaVolume(value: Int)
```

#### Set Ringtone Volume

Sets the ringtone volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**:
    *   0 to 15: Models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `SL25`, `PC10`
    *   0 to 7: All other models
*   **Parameters**:
    *   `value` (Int): The desired volume level.

```kotlin
fun setRingtoneVolume(value: Int)
```

#### Set Notification Volume

Sets the notification volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Requires Android Version**: Android 14 (UpsideDownCake) or later
*   **Range**: Same as `setRingtoneVolume`
*   **Parameters**:
    *   `value` (Int): The desired volume level.

```kotlin
fun setNotificationVolume(value: Int)
```

#### Set Alarm Volume

Sets the alarm volume level.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Range**:
    *   0 to 15: Models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `PC10`
    *   0 to 7: All other models
*   **Parameters**:
    *   `value` (Int): The desired volume level.

```kotlin
fun setAlarmVolume(value: Int)
```

#### Enable Vibration Mode

Enables vibration mode. This sets ringtone and notification volumes to 0.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
fun enableVibrationMode()
```

#### Disable Vibration Mode

Disables vibration mode.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
fun disableVibrationMode()
```

#### Set Display Settings

Configures display settings.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `displaySetting` (DisplaySetting): Object containing display configuration.

```kotlin
fun setDisplaySetting(displaySetting: DisplaySetting)
```

#### Get Serial Number

Retrieves the device's serial number.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Returns**: The serial number string.

```kotlin
// Coroutine (for kotlin)
suspend fun getSerialNumber(): String

// Callback (for java)
fun getSerialNumber(callback: RequestCallback<String>): Job
```

---

### Network API

Configures mobile network settings.

#### Set APN

Sets the Access Point Name (APN) configuration.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `apn` (Apn): The `Apn` object containing configuration details. Use `Apn.Builder` to create an instance.

```kotlin
fun setApn(apn: Apn)
```

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
fun grantPermission(packageName: String, permission: String)
```

#### Revoke Permission

Revokes a specific runtime permission from a target application package.

*   **Requires StartUp Version**: `6.4.17` or later
*   **Parameters**:
    *   `packageName` (String): The package name of the application.
    *   `permission` (String): The fully qualified name of the permission to revoke.

```kotlin
fun revokePermission(packageName: String, permission: String)
```

---

### Quick Tile API

Customizes the Quick Settings tiles in the System UI.

#### Set Quick Tiles

Sets the Quick Tiles to be displayed.

*   **Requires StartUp Version**: `6.4.1` or later
*   **Parameters**:
    *   `quickTile` (vararg QuickTile): One or more `QuickTile` objects to add.

```kotlin
fun setQuickTiles(vararg quickTile: QuickTile)
```

#### Reset Quick Tiles

Resets the Quick Tiles configuration to the default state.

*   **Requires StartUp Version**: `6.4.1` or later

```kotlin
fun resetQuickTile()
```

---

### StartUp Setting API

Manages the StartUp SDK's own settings.

#### Reset StartUp Settings

Resets the StartUp settings to their default values.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
fun resetStartUpSetting()
```

---

### Time API

Configures system time, timezone, and NTP server settings.

#### Set Date and Time

Sets the date and time of the device.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Parameters**:
    *   `dateTime` (java.time.LocalDateTime): The date and time to set.

```kotlin
fun setDateTime(dateTime: LocalDateTime)
```

#### Set NTP Server

Sets the NTP server for automatic time synchronization. This setting takes effect after the next reboot.

*   **Requires StartUp Version**: `6.4.9` or later
*   **Parameters**:
    *   `host` (String): The hostname or IP address of the NTP server.

```kotlin
fun setNtpServer(host: String)
```

#### Set Timezone

Sets the system's default timezone.

*   **Requires StartUp Version**: `6.5.9` or later
*   **Parameters**:
    *   `timezone` (String): The timezone identifier (e.g., "America/New_York").

```kotlin
fun setTimeZone(timezone: String)
```

#### Get NTP Server

Retrieves the currently configured NTP server address.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP server address as a String.

```kotlin
fun getNtpServer(): String
```

#### Get NTP Interval

Retrieves the currently configured NTP synchronization interval.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The NTP synchronization interval in milliseconds (Int).

```kotlin
fun getNtpInterval(): Int
```

#### Get Timezone

Retrieves the system's current default timezone.

*   **Returns**: The timezone identifier as a String.

```kotlin
fun getTimeZone(): String
```

---

### Usb API

Configures the USB connection mode of the device.

#### Set USB Mode to MTP

Sets the USB connection mode to MTP (Media Transfer Protocol).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```kotlin
fun setUsbModeMtp()
```

#### Set USB Mode to RNDIS

Sets the USB connection mode to RNDIS (USB Tethering).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```kotlin
fun setUsbModeRndis()
```

#### Set USB Mode to MIDI

Sets the USB connection mode to MIDI.

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```kotlin
fun setUsbModeMidi()
```

#### Set USB Mode to PTP

Sets the USB connection mode to PTP (Picture Transfer Protocol).

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```kotlin
fun setUsbModePtp()
```

#### Disable USB Data (Charging Only)

Disables all USB data connections, setting the mode to charging only.

*   **Supported Models**: `US20`, `US30`
*   **Requires StartUp Version**: `6.5.10` or later

```kotlin
fun setUsbModeNone()
```

#### Get Current USB Modes

Retrieves the current USB connection mode.

*   **Returns**: A list of strings representing the currently active USB modes. Returns an empty list if no active modes are found.

```kotlin
fun getCurrentUsbModes(): List<String>
```

---

### Wifi API

Provides comprehensive control over Wi-Fi settings and configurations.

#### Get Wi-Fi MAC Address

Retrieves the Wi-Fi MAC address of the device.

*   **Requires StartUp Version**: `6.4.11` or later

```kotlin
// Coroutine (for kotlin)
suspend fun getWifiMac(): String

// Callback (for java)
fun getWifiMac(callback: RequestCallback<String>): Job
```

#### Captive Portal Detection

Controls whether the device detects captive portals (login pages for public Wi-Fi).

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL20`

```kotlin
fun enableCaptivePortalDetection()
fun disableCaptivePortalDetection()
```

#### Frequency Band Control

Restricts the Wi-Fi frequency band usage.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SM15`, `SL10`, `SL10K`

```kotlin
fun allowAllWifiFrequencyBand()
fun allowOnly2_4GHzWifiFrequencyBand()
fun allowOnly5GHzWifiFrequencyBand()
```

#### Set Wi-Fi Country

Sets the Wi-Fi country code.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SL10`, `SL10K`
*   **Parameters**:
    *   `countryCode` (String): ISO 3166-1 alpha-2 country code (e.g., "US", "KR").

```kotlin
fun setWifiCountry(countryCode: String)
```

#### Open Network Notification

Controls notifications for available open Wi-Fi networks.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
fun enableOpenNetworkNotification()
fun disableOpenNetworkNotification()
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

```kotlin
fun setRoamingTrigger(index: Int)
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

```kotlin
fun setRoamingDelta(index: Int)
```

#### Wi-Fi Sleep Policy

Controls when Wi-Fi should go to sleep.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
fun setWifiSleepPolicyNever()         // Keep Wi-Fi on always
fun setWifiSleepPolicyPluggedOnly()   // Keep on when plugged in
fun setWifiSleepPolicyAlways()        // Allow sleep when screen is off
```

#### Wi-Fi Stability

Optimizes Wi-Fi performance.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Note**: Not supported on Android 13 or later.

```kotlin
fun setWifiStabilityNormal() // Balanced
fun setWifiStabilityHigh()   // Performance focused (High battery usage)
```

#### Set Wi-Fi Channels

Sets the allowed Wi-Fi channels.

*   **Requires StartUp Version**: `6.2.14` or later
*   **Unsupported Models**: `SM15`, `SL10`, `SL10K`
*   **Parameters**:
    *   `channels` (vararg Int): List of channels to enable (e.g., 1, 6, 11, 36).

```kotlin
fun setWifiChannel(vararg channels: Int)
```

#### Network Management

##### Set Access Point

Configures a Wi-Fi Access Point.

*   **Requires StartUp Version**: `6.2.14` or later

```kotlin
fun setAccessPoint(accessPoint: AccessPoint)
```

##### Clear Saved Wi-Fi Networks

Removes all saved Wi-Fi networks.

*   **Requires StartUp Version**: `6.4.11` or later

```kotlin
fun clearSavedWifiNetworks()
```

##### Remove Wi-Fi Network

Removes a specific Wi-Fi network.

*   **Requires StartUp Version**: `6.4.11` or later

```kotlin
fun removeWifiNetwork(ssid: String)
```

#### Device Specific Wi-Fi Settings

These functions are available on specific devices.

##### Get Roaming Threshold

Retrieves the current Wi-Fi roaming threshold value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming threshold as a negative `Int`.

```kotlin
fun getRoamingThreshold(): Int
```

##### Get Roaming Delta

Retrieves the current Wi-Fi roaming delta value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The roaming delta as an `Int`.

```kotlin
fun getRoamingDelta(): Int
```

##### Get Wi-Fi Frequency Band

Retrieves the current preferred Wi-Fi frequency band value.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: An `Int` representing the frequency band:
    *   `0`: Automatic
    *   `1`: 5 GHz Only
    *   `2`: 2.4 GHz Only

```kotlin
fun getWifiFrequencyBand(): Int
```

##### Get Wi-Fi Country Code

Retrieves the current Wi-Fi country code.

*   **Supported Models**: `US20`, `US30`
*   **Returns**: The country code as a `String`.

```kotlin
fun getWifiCountryCode(): String
```
