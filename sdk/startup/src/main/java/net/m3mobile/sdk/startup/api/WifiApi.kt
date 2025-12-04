package net.m3mobile.sdk.startup.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.RequiresStartUp
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsupportedModels
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.sdk.startup.params.AccessPoint

interface WifiApi {

    /**
     * Retrieves the Wi-Fi MAC address of the device.
     *
     * StartUp version `6.4.11` or later is required.
     *
     * @return The Wi-Fi MAC address as a String.
     */
    @RequiresStartUp("6.4.11")
    suspend fun getWifiMac(): String

    /**
     * Asynchronously gets the device's Wi-Fi MAC address.
     *
     * StartUp version `6.4.11` or later is required.
     *
     * @param callback A callback to handle the result.
     * @return A [Job] representing the coroutine that is executing the request.
     */
    @RequiresStartUp("6.4.11")
    fun getWifiMac(callback: RequestCallback<String>): Job

    /**
     * Enables captive portal detection for Wi-Fi.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @UnsupportedModels(DeviceModel.SL20)
    @RequiresStartUp("6.2.14")
    fun enableCaptivePortalDetection()

    /**
     * Disables captive portal detection for Wi-Fi.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @UnsupportedModels(DeviceModel.SL20)
    @RequiresStartUp("6.2.14")
    fun disableCaptivePortalDetection()

    /**
     * Allows the use of all Wi-Fi frequency bands supported by the device.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    @RequiresStartUp("6.2.14")
    fun allowAllWifiFrequencyBand()

    /**
     * Restricts the Wi-Fi frequency band to 2.4GHz only.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    @RequiresStartUp("6.2.14")
    fun allowOnly2_4GHzWifiFrequencyBand()

    /**
     * Restricts the Wi-Fi frequency band to 5GHz only.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    @RequiresStartUp("6.2.14")
    fun allowOnly5GHzWifiFrequencyBand()

    /**
     * Sets the Wi-Fi country code.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param countryCode The two-letter ISO 3166-1 alpha-2 country code (e.g., "US", "KR", "DE").
     */
    @UnsupportedModels(DeviceModel.SL10, DeviceModel.SL10K)
    @RequiresStartUp("6.2.14")
    fun setWifiCountry(countryCode: String)

    /**
     * Enables the "Open network notification".
     *
     * When this is enabled, the device will notify the user whenever an open Wi-Fi network
     * is available in the vicinity.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    fun enableOpenNetworkNotification()

    /**
     * Disables the "Open network notification".
     *
     * When this is disabled, the device will no longer notify the user when an open
     * Wi-Fi network is available.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    fun disableOpenNetworkNotification()

    /**
     * Sets the Wi-Fi roaming trigger level.
     *
     * This function defines the signal strength (RSSI) threshold at which the device will
     * start scanning for a better access point to roam to. A lower value means the device
    ll wait longer before looking for a new AP, while a higher value will make it roam more
     * aggressively.
     *
     * This setting works in conjunction with [setRoamingDelta].
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param index An index between 0 and 4
     * - `0`: -80dBm
     * - `1`: -75dBm
     * - `2`: -70dBm
     * - `3`: -65dBm
     * - `4`: -60dBm
     */
    @UnsupportedModels(DeviceModel.SL10K, DeviceModel.SL10)
    @RequiresStartUp("6.2.14")
    fun setRoamingTrigger(index: Int)

    /**
     * Sets the Wi-Fi roaming delta.
     *
     * This value represents the minimum required signal strength difference (in dB) between
     * the current access point (AP) and a potential new AP for a roam to occur. A larger delta
     * makes roaming less likely, as the new AP must be significantly stronger. A smaller delta
     * allows for more frequent roaming.
     *
     * This setting works in conjunction with [setRoamingTrigger].
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param index An index between 0 and 6
     * - `0`: 30dB
     * - `1`: 25dB
     * - `2`: 20dB
     * - `3`: 15dB
     * - `4`: 10dB
     * - `5`: 5dB
     * - `6`: 0dB
     */
    @UnsupportedModels(DeviceModel.SL10K, DeviceModel.SL10)
    @RequiresStartUp("6.2.14")
    fun setRoamingDelta(index: Int)

    /**
     * Sets the Wi-Fi sleep policy to "Never".
     *
     * This keeps the Wi-Fi on at all times, even when the device's screen is off.
     * This can increase power consumption but ensures continuous connectivity.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    fun setWifiSleepPolicyNever()

    /**
     * Sets the Wi-Fi sleep policy to "Only when plugged in".
     *
     * With this policy, the Wi-Fi be kept on when the device is connected to a power
     * source (charging). When on battery power, Wi-Fi will sleep when the screen turns off.
     * This offers a balance between connectivity and battery life.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    fun setWifiSleepPolicyPluggedOnly()

    /**
     * Sets the Wi-Fi sleep policy to "Always" (also known as "While screen is off").
     *
     * This allows the Wi-Fi to turn off when the device's screen is off and it's
     * not connected to a power source, in order to save battery. Wi-Fi will automatically
     * reconnect when the screen is turned back on.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    fun setWifiSleepPolicyAlways()

    /**
     * Sets the Wi-Fi stability to the "Normal" level.
     *
     * This mode offers a standard balance between Wi-Fi performance and power consumption,
     * suitable for general use cases.
     *
     * Please note that this feature does not work on Android 13 or later.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @see setWifiStabilityHigh
     */
    @RequiresStartUp("6.2.14")
    fun setWifiStabilityNormal()

    /**
     * Sets Wi-Fi stability to the "High" level.
     *
     * This mode optimizes Wi-Fi for performance by keeping the CPU active, which can
     * increase power consumption.
     *
     * Please note that this feature does not work on Android 13 or later.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @see setWifiStabilityNormal
     */
    @RequiresStartUp("6.2.14")
    fun setWifiStabilityHigh()

    /**
     * Sets the available Wi-Fi channels for the device to scan and connect to.
     *
     * This function allows you to restrict the device's Wi-Fi operations to a specific set of channels.
     * By default, all channels supported by the device's regulatory domain (country code) are enabled.
     * Use this to limit scanning and connection attempts to only the channels used by your network infrastructure,
     * which can improve connection times and reduce interference.
     *
     * **Note:** When using both 2.4GHz and 5GHz bands, all desired channels for both bands must be
     * passed in a single call to this method. The device does not retain previous settings;
     * each call overwrites the existing channel list.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param channels A variable number of integer arguments representing the Wi-Fi channels to enable.
     *                 For example, to enable 2.4GHz channels 1, 6, 11 and 5GHz channels 36, 40,
     *                 you would call `setWifiChannel(1, 6, 11, 36, 40)`.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    @RequiresStartUp("6.2.14")
    fun setWifiChannel(vararg channels: Int)

    /**
     * Configures a specific Wi-Fi Access Point (AP) on the device.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param accessPoint
     * @see AccessPoint.Builder
     */
    @RequiresStartUp("6.2.14")
    fun setAccessPoint(accessPoint: AccessPoint)

    /**
     * Removes all configured Wi-Fi networks from the device.
     *
     * StartUp version `6.4.11` or later is required.
     */
    @RequiresStartUp("6.4.11")
    fun clearSavedWifiNetworks()

    /**
     * Removes a specific Wi-Fi network configuration from the device.
     *
     * StartUp version `6.4.11` or later is required.
     *
     * @param ssid SSID of the network to remove.
     */
    @RequiresStartUp("6.4.11")
    fun removeWifiNetwork(ssid: String)

    /**
     * Retrieves the current Wi-Fi roaming threshold value.
     *
     * @return The roaming threshold as a negative `Int`
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun getRoamingThreshold(): Int

    /**
     * Retrieves the current Wi-Fi roaming delta value.
     *
     * @return The roaming delta
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun getRoamingDelta(): Int
}