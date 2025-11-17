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
     * Sets the Wi-Fi frequency band.
     *
     * @param band The desired [WifiFrequencyBandMode] to set.
     */
    @UnsupportedModels(DeviceModel.SM15, DeviceModel.SL10, DeviceModel.SL10K)
    fun setWifiFrequencyBand(band: WifiFrequencyBandMode)
}