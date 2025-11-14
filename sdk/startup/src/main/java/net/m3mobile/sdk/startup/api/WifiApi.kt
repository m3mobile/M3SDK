package net.m3mobile.sdk.startup.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback

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
}