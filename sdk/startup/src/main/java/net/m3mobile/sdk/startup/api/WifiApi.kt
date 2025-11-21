package net.m3mobile.sdk.startup.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.UnsupportedModels
import net.m3mobile.core.device.DeviceModel

interface WifiApi {

    /**
     * Retrieves the Wi-Fi MAC address of the device.
     *
     * This is a suspend function and should be called from a coroutine.
     *
     * @return The Wi-Fi MAC address as a String.
     */
    suspend fun getWifiMac(): String

    /**
     * Asynchronously gets the device's Wi-Fi MAC address.
     *
     * @param callback A callback to handle the result.
     * @return A [Job] representing the coroutine that is executing the request.
     */
    fun getWifiMac(callback: RequestCallback<String>): Job

    /**
     * Enables captive portal detection for Wi-Fi.
     */
    @UnsupportedModels(DeviceModel.SL20)
    fun enableCaptivePortalDetection()

    /**
     * Disables captive portal detection for Wi-Fi.
     */
    @UnsupportedModels(DeviceModel.SL20)
    fun disableCaptivePortalDetection()

    /**
     * Allows the use of all Wi-Fi frequency bands supported by the device.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    fun allowAllWifiFrequencyBand()

    /**
     * Restricts the Wi-Fi frequency band to 2.4GHz only.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    fun allowOnly2_4GHzWifiFrequencyBand()

    /**
     * Restricts the Wi-Fi frequency band to 5GHz only.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    fun allowOnly5GHzWifiFrequencyBand()

    /**
     * Sets the Wi-Fi country code.
     *
     * @param countryCode The two-letter ISO 3166-1 alpha-2 country code (e.g., "US", "KR", "DE").
     */
    @UnsupportedModels(DeviceModel.SL10, DeviceModel.SL10K)
    fun setWifiCountry(countryCode: String)

    /**
     * Enables the "Open network notification".
     *
     * When this is enabled, the device will notify the user whenever an open Wi-Fi network
     * is available in the vicinity.
     */
    fun enableOpenNetworkNotification()

    /**
     * Disables the "Open network notification".
     *
     * When this is disabled, the device will no longer notify the user when an open
     * Wi-Fi network is available.
     */
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
     * @param index An index between 0 and 4
     * - `0`: -80dBm
     * - `1`: -75dBm
     * - `2`: -70dBm
     * - `3`: -65dBm
     * - `4`: -60dBm
     */
    @UnsupportedModels(DeviceModel.SL10K, DeviceModel.SL10)
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
    fun setRoamingDelta(index: Int)

    /**
     * Sets the Wi-Fi sleep policy to "Never".
     *
     * This keeps the Wi-Fi on at all times, even when the device's screen is off.
     * This can increase power consumption but ensures continuous connectivity.
     */
    fun setWifiSleepPolicyNever()

    /**
     * Sets the Wi-Fi sleep policy to "Only when plugged in".
     *
     * With this policy, the Wi-Fi be kept on when the device is connected to a power
     * source (charging). When on battery power, Wi-Fi will sleep when the screen turns off.
     * This offers a balance between connectivity and battery life.
     */
    fun setWifiSleepPolicyPluggedOnly()

    /**
     * Sets the Wi-Fi sleep policy to "Always" (also known as "While screen is off").
     *
     * This allows the Wi-Fi to turn off when the device's screen is off and it's
     * not connected to a power source, in order to save battery. Wi-Fi will automatically
     * reconnect when the screen is turned back on.
     */
    fun setWifiSleepPolicyAlways()

    /**
     * Sets the Wi-Fi stability to the "Normal" level.
     *
     * This mode offers a standard balance between Wi-Fi performance and power consumption,
     * suitable for general use cases.
     *
     * Please note that this feature does not work on Android 13 or later.
     *
     * @see setWifiStabilityHigh
     */
    fun setWifiStabilityNormal()

    /**
     * Sets Wi-Fi stability to the "High" level.
     *
     * This mode optimizes Wi-Fi for performance by keeping the CPU active, which can
     * increase power consumption.
     *
     * Please note that this feature does not work on Android 13 or later.
     *
     * @see setWifiStabilityNormal
     */
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
     * @param channels A variable number of integer arguments representing the Wi-Fi channels to enable.
     *                 For example, to enable 2.4GHz channels 1, 6, 11 and 5GHz channels 36, 40,
     *                 you would call `setWifiChannel(1, 6, 11, 36, 40)`.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    fun setWifiChannel(vararg channels: Int)

    /**
     * Configures a specific Wi-Fi Access Point (AP) on the device.
     *
     * @param accessPoint
     * @see AccessPoint.Builder
     */
    fun setAccessPoint(accessPoint: AccessPoint)

    /**
     * Removes all configured Wi-Fi networks from the device.
     */
    fun clearSavedWifiNetworks()

    /**
     * Removes a specific Wi-Fi network configuration from the device.
     *
     * @param ssid SSID of the network to remove.
     */
    fun removeWifiNetwork(ssid: String)
}