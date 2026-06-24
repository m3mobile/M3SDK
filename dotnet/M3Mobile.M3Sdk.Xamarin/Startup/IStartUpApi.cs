using System;
using System.Threading;
using System.Threading.Tasks;

namespace M3Sdk.Xamarin.Startup
{
    /// <summary>
    /// StartUp app API contract implemented through native Android broadcasts.
    /// </summary>
    public interface IStartUpApi
    {
        /// <summary>
        /// Turns on airplane mode.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.3.7</c> or later.</remarks>
        void TurnOnAirplaneMode();

        /// <summary>
        /// Turns off airplane mode.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.3.7</c> or later.</remarks>
        void TurnOffAirplaneMode();

        /// <summary>
        /// Installs an APK from a local file path.
        /// </summary>
        /// <param name="filePath">The absolute path to the APK file to install.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void InstallLocalApk(string filePath);

        /// <summary>
        /// Installs an APK from a remote URL.
        /// </summary>
        /// <param name="url">The URL of the APK file to install.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void InstallRemoteApk(string url);

        /// <summary>
        /// Enables the specified application package.
        /// </summary>
        /// <param name="packageName">The package name of the application to enable.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void EnableApp(string packageName);

        /// <summary>
        /// Disables the specified application package.
        /// </summary>
        /// <param name="packageName">The package name of the application to disable.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void DisableApp(string packageName);

        /// <summary>
        /// Enables and runs the specified application package.
        /// </summary>
        /// <param name="packageName">The package name of the application to enable and run.</param>
        /// <remarks>Requires StartUp version <c>6.8.0</c> or later.</remarks>
        void RunApp(string packageName);

        /// <summary>
        /// Enables, runs, and pins the specified application package.
        /// </summary>
        /// <param name="packageName">The package name of the application to enable, run, and pin.</param>
        /// <remarks>Requires StartUp version <c>6.8.0</c> or later.</remarks>
        void RunAndPinApp(string packageName);

        /// <summary>
        /// Sets the media volume level.
        /// </summary>
        /// <param name="value">The media volume level. Kotlin KDoc defines the supported range as <c>0</c> to <c>15</c>.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetMediaVolume(int value);

        /// <summary>
        /// Sets the ringtone volume level.
        /// </summary>
        /// <param name="value">The ringtone volume level. Kotlin KDoc defines <c>0</c> to <c>15</c> for SL10, SL10K, SL20, SL20K, SL20P, SL25, and PC10; otherwise <c>0</c> to <c>7</c>.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetRingtoneVolume(int value);

        /// <summary>
        /// Sets the notification volume level.
        /// </summary>
        /// <param name="value">The notification volume level. Kotlin KDoc defines <c>0</c> to <c>15</c> for SL10, SL10K, SL20, SL20K, SL20P, SL25, and PC10; otherwise <c>0</c> to <c>7</c>.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetNotificationVolume(int value);

        /// <summary>
        /// Sets the alarm volume level.
        /// </summary>
        /// <param name="value">The alarm volume level. Kotlin KDoc defines <c>0</c> to <c>15</c> for SL10, SL10K, SL20, SL20K, SL20P, and PC10; otherwise <c>0</c> to <c>7</c>.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetAlarmVolume(int value);

        /// <summary>
        /// Enables vibration mode.
        /// </summary>
        /// <remarks>When vibration mode is enabled, ringtone and notification volume are automatically set to <c>0</c>. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void EnableVibrationMode();

        /// <summary>
        /// Disables vibration mode.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void DisableVibrationMode();

        /// <summary>
        /// Applies the device display configuration.
        /// </summary>
        /// <param name="displaySetting">The display configuration to apply.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetDisplaySetting(DisplaySetting displaySetting);

        /// <summary>
        /// Asynchronously gets the device serial number.
        /// </summary>
        /// <returns>A task that resolves to the device serial number.</returns>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        Task<string> GetSerialNumberAsync();

        /// <summary>
        /// Asynchronously gets the device serial number.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the device serial number.</returns>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        Task<string> GetSerialNumberAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the device serial number and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the serial number or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        IM3Cancelable GetSerialNumber(M3RequestCallback<string> callback);

        /// <summary>
        /// Asynchronously gets the device Bluetooth MAC address.
        /// </summary>
        /// <returns>A task that resolves to the Bluetooth MAC address.</returns>
        /// <remarks>Requires StartUp version <c>6.5.35</c> or later; for UL30, <c>6.5.31</c> or later is required.</remarks>
        Task<string> GetBluetoothMacAsync();

        /// <summary>
        /// Asynchronously gets the device Bluetooth MAC address.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the Bluetooth MAC address.</returns>
        /// <remarks>Requires StartUp version <c>6.5.35</c> or later; for UL30, <c>6.5.31</c> or later is required.</remarks>
        Task<string> GetBluetoothMacAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the device Bluetooth MAC address and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the Bluetooth MAC address or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires StartUp version <c>6.5.35</c> or later; for UL30, <c>6.5.31</c> or later is required.</remarks>
        IM3Cancelable GetBluetoothMac(M3RequestCallback<string> callback);

        /// <summary>
        /// Locks status bar expansion so the user cannot pull down notifications or quick settings.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.4.12</c> or later.</remarks>
        void LockStatusBarExpansion();

        /// <summary>
        /// Unlocks status bar expansion.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.4.12</c> or later.</remarks>
        void UnlockStatusBarExpansion();

        /// <summary>
        /// Sets the device system language.
        /// </summary>
        /// <param name="language">The language code, such as <c>en</c> or <c>ko</c>.</param>
        /// <param name="country">The country code, such as <c>US</c> or <c>KR</c>.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetLanguage(string language, string country);

        /// <summary>
        /// Sets the Access Point Name (APN) configuration used for mobile data.
        /// </summary>
        /// <param name="apn">The APN configuration to apply.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetApn(Apn apn);

        /// <summary>
        /// Enables Near Field Communication (NFC).
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void EnableNfc();

        /// <summary>
        /// Disables Near Field Communication (NFC).
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void DisableNfc();

        /// <summary>
        /// Grants a runtime permission to a target application package.
        /// </summary>
        /// <param name="packageName">The package name of the application that receives the permission.</param>
        /// <param name="permission">The fully qualified permission name to grant.</param>
        /// <remarks>Requires StartUp version <c>6.4.17</c> or later.</remarks>
        void GrantPermission(string packageName, string permission);

        /// <summary>
        /// Revokes a runtime permission from a target application package.
        /// </summary>
        /// <param name="packageName">The package name of the application whose permission is revoked.</param>
        /// <param name="permission">The fully qualified permission name to revoke.</param>
        /// <remarks>Requires StartUp version <c>6.4.17</c> or later.</remarks>
        void RevokePermission(string packageName, string permission);

        /// <summary>
        /// Adds quick settings tiles to the system UI.
        /// </summary>
        /// <param name="quickTiles">The quick settings tiles to add.</param>
        /// <remarks>Requires StartUp version <c>6.4.1</c> or later.</remarks>
        void SetQuickTiles(params QuickTile[] quickTiles);

        /// <summary>
        /// Resets the quick settings tile configuration to the default state.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.4.1</c> or later.</remarks>
        void ResetQuickTile();

        /// <summary>
        /// Resets StartUp settings to their default values.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void ResetStartUpSetting();

        /// <summary>
        /// Sets the device date and time.
        /// </summary>
        /// <param name="dateTime">The local date and time to apply.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetDateTime(DateTime dateTime);

        /// <summary>
        /// Sets the device date and time from a <see cref="DateTimeOffset" /> value.
        /// </summary>
        /// <param name="dateTime">The date and time value whose local date-time component is applied.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetDateTime(DateTimeOffset dateTime);

        /// <summary>
        /// Sets the device date and time from individual date-time fields.
        /// </summary>
        /// <param name="year">The year component.</param>
        /// <param name="month">The month component.</param>
        /// <param name="day">The day component.</param>
        /// <param name="hour">The hour component.</param>
        /// <param name="minute">The minute component.</param>
        /// <param name="second">The second component.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetDateTime(int year, int month, int day, int hour, int minute, int second);

        /// <summary>
        /// Sets the NTP server used for automatic time synchronization.
        /// </summary>
        /// <param name="host">The host name or IP address of the NTP server.</param>
        /// <remarks>This setting takes effect from the next boot. Requires StartUp version <c>6.4.9</c> or later.</remarks>
        void SetNtpServer(string host);

        /// <summary>
        /// Sets the system default time zone.
        /// </summary>
        /// <param name="timezone">The time zone identifier, such as <c>America/New_York</c>.</param>
        /// <remarks>Requires StartUp version <c>6.5.9</c> or later.</remarks>
        void SetTimeZone(string timezone);

        /// <summary>
        /// Sets the USB connection mode to MTP.
        /// </summary>
        /// <remarks>Supported on US20 and US30. Requires StartUp version <c>6.5.10</c> or later.</remarks>
        void SetUsbModeMtp();

        /// <summary>
        /// Sets the USB connection mode to RNDIS.
        /// </summary>
        /// <remarks>Supported on US20 and US30. Requires StartUp version <c>6.5.10</c> or later.</remarks>
        void SetUsbModeRndis();

        /// <summary>
        /// Sets the USB connection mode to MIDI.
        /// </summary>
        /// <remarks>Supported on US20 and US30. Requires StartUp version <c>6.5.10</c> or later.</remarks>
        void SetUsbModeMidi();

        /// <summary>
        /// Sets the USB connection mode to PTP.
        /// </summary>
        /// <remarks>Supported on US20 and US30. Requires StartUp version <c>6.5.10</c> or later.</remarks>
        void SetUsbModePtp();

        /// <summary>
        /// Disables USB data connections, setting USB to charging-only mode.
        /// </summary>
        /// <remarks>Supported on US20 and US30. Requires StartUp version <c>6.5.10</c> or later.</remarks>
        void SetUsbModeNone();

        /// <summary>
        /// Asynchronously gets the device Wi-Fi MAC address.
        /// </summary>
        /// <returns>A task that resolves to the Wi-Fi MAC address.</returns>
        /// <remarks>Requires StartUp version <c>6.4.11</c> or later.</remarks>
        Task<string> GetWifiMacAsync();

        /// <summary>
        /// Asynchronously gets the device Wi-Fi MAC address.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the Wi-Fi MAC address.</returns>
        /// <remarks>Requires StartUp version <c>6.4.11</c> or later.</remarks>
        Task<string> GetWifiMacAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the device Wi-Fi MAC address and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the Wi-Fi MAC address or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires StartUp version <c>6.4.11</c> or later.</remarks>
        IM3Cancelable GetWifiMac(M3RequestCallback<string> callback);

        /// <summary>
        /// Asynchronously gets the factory Wi-Fi MAC address.
        /// </summary>
        /// <returns>A task that resolves to the factory Wi-Fi MAC address response.</returns>
        /// <remarks>Requires StartUp version <c>6.7.3</c> or later.</remarks>
        Task<FactoryWifiMacResult> GetFactoryWifiMacAsync();

        /// <summary>
        /// Asynchronously gets the factory Wi-Fi MAC address.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the factory Wi-Fi MAC address response.</returns>
        /// <remarks>Requires StartUp version <c>6.7.3</c> or later.</remarks>
        Task<FactoryWifiMacResult> GetFactoryWifiMacAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the factory Wi-Fi MAC address and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the factory Wi-Fi MAC address response or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires StartUp version <c>6.7.3</c> or later.</remarks>
        IM3Cancelable GetFactoryWifiMac(M3RequestCallback<FactoryWifiMacResult> callback);

        /// <summary>
        /// Enables captive portal detection for Wi-Fi.
        /// </summary>
        /// <remarks>Unsupported on SL20. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void EnableCaptivePortalDetection();

        /// <summary>
        /// Disables captive portal detection for Wi-Fi.
        /// </summary>
        /// <remarks>Unsupported on SL20. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void DisableCaptivePortalDetection();

        /// <summary>
        /// Allows all Wi-Fi frequency bands supported by the device.
        /// </summary>
        /// <remarks>Unsupported on SM15, SL10, and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void AllowAllWifiFrequencyBand();

        /// <summary>
        /// Restricts Wi-Fi to the 2.4 GHz frequency band.
        /// </summary>
        /// <remarks>Unsupported on SM15, SL10, and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void AllowOnly2_4GHzWifiFrequencyBand();

        /// <summary>
        /// Restricts Wi-Fi to the 5 GHz frequency band.
        /// </summary>
        /// <remarks>Unsupported on SM15, SL10, and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void AllowOnly5GHzWifiFrequencyBand();

        /// <summary>
        /// Sets the Wi-Fi country code.
        /// </summary>
        /// <param name="countryCode">The two-letter ISO 3166-1 alpha-2 country code, such as <c>US</c>, <c>KR</c>, or <c>DE</c>.</param>
        /// <remarks>Unsupported on SL10 and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiCountry(string countryCode);

        /// <summary>
        /// Enables the open network notification.
        /// </summary>
        /// <remarks>When enabled, the device notifies the user when an open Wi-Fi network is available. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void EnableOpenNetworkNotification();

        /// <summary>
        /// Disables the open network notification.
        /// </summary>
        /// <remarks>When disabled, the device no longer notifies the user when an open Wi-Fi network is available. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void DisableOpenNetworkNotification();

        /// <summary>
        /// Sets the Wi-Fi roaming trigger level.
        /// </summary>
        /// <param name="index">An index from <c>0</c> to <c>4</c>: <c>0</c> = -80 dBm, <c>1</c> = -75 dBm, <c>2</c> = -70 dBm, <c>3</c> = -65 dBm, <c>4</c> = -60 dBm.</param>
        /// <remarks>Works with <see cref="SetRoamingDelta" />. Unsupported on SL10 and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetRoamingTrigger(int index);

        /// <summary>
        /// Sets the Wi-Fi roaming delta.
        /// </summary>
        /// <param name="index">An index from <c>0</c> to <c>6</c>: <c>0</c> = 30 dB, <c>1</c> = 25 dB, <c>2</c> = 20 dB, <c>3</c> = 15 dB, <c>4</c> = 10 dB, <c>5</c> = 5 dB, <c>6</c> = 0 dB.</param>
        /// <remarks>Works with <see cref="SetRoamingTrigger" />. Unsupported on SL10 and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetRoamingDelta(int index);

        /// <summary>
        /// Sets the Wi-Fi sleep policy to keep Wi-Fi on at all times.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiSleepPolicyNever();

        /// <summary>
        /// Sets the Wi-Fi sleep policy to keep Wi-Fi on only while the device is plugged in.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiSleepPolicyPluggedOnly();

        /// <summary>
        /// Sets the Wi-Fi sleep policy to allow Wi-Fi to turn off while the screen is off.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiSleepPolicyAlways();

        /// <summary>
        /// Sets Wi-Fi stability to normal.
        /// </summary>
        /// <remarks>Kotlin KDoc notes that this feature does not work on Android 13 or later. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiStabilityNormal();

        /// <summary>
        /// Sets Wi-Fi stability to high.
        /// </summary>
        /// <remarks>This mode keeps the CPU active for Wi-Fi performance and can increase power consumption. Kotlin KDoc notes that this feature does not work on Android 13 or later. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiStabilityHigh();

        /// <summary>
        /// Sets the Wi-Fi channels the device may scan and connect to.
        /// </summary>
        /// <param name="channels">The Wi-Fi channels to enable. Passing channels overwrites the existing channel list.</param>
        /// <remarks>Unsupported on SM15, SL10, and SL10K. Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetWifiChannel(params int[] channels);

        /// <summary>
        /// Configures a Wi-Fi access point on the device.
        /// </summary>
        /// <param name="accessPoint">The access point configuration to apply.</param>
        /// <remarks>Requires StartUp version <c>6.2.14</c> or later.</remarks>
        void SetAccessPoint(AccessPoint accessPoint);

        /// <summary>
        /// Removes all configured Wi-Fi networks from the device.
        /// </summary>
        /// <remarks>Requires StartUp version <c>6.4.11</c> or later.</remarks>
        void ClearSavedWifiNetworks();

        /// <summary>
        /// Removes a specific Wi-Fi network configuration from the device.
        /// </summary>
        /// <param name="ssid">The SSID of the network to remove.</param>
        /// <remarks>Requires StartUp version <c>6.4.11</c> or later.</remarks>
        void RemoveWifiNetwork(string ssid);
    }
}
