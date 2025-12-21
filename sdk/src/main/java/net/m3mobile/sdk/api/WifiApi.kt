package net.m3mobile.sdk.api

import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

public interface WifiApi {

    /**
     * Retrieves the current Wi-Fi roaming threshold value.
     *
     * @return The roaming threshold as a negative `Int`
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    public fun getRoamingThreshold(): Int

    /**
     * Retrieves the current Wi-Fi roaming delta value.
     *
     * @return The roaming delta
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    public fun getRoamingDelta(): Int

    /**
     * Retrieves the current preferred Wi-Fi frequency band value.
     *
     * @return
     * - `0` - Automatic
     * - `1` - 5 GHz Only
     * - `2` - 2.4 GHz Only
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    public fun getWifiFrequencyBand(): Int

    /**
     * Retrieves the current Wi-Fi country code.
     *
     * @return The country code
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    public fun getWifiCountryCode(): String
}