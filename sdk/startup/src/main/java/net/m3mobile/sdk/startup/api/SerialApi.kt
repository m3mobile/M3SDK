package net.m3mobile.sdk.startup.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback

interface SerialApi {

    /**
     * Gets the serial number of the device.
     *
     * This is a suspending function and must be called from a coroutine scope.
     *
     * @return The serial number of the device as a [String].
     */
    suspend fun getSerialNumber(): String

    /**
     * Asynchronously retrieves the device's serial number.
     *
     * This function initiates a request to get the serial number and returns the result
     * via a callback on the main thread.
     *
     * @param callback A callback to receive the result.
     * @return A [Job] representing the coroutine that is executing the request. This can be used to cancel the operation.
     */
    fun getSerialNumber(callback: RequestCallback<String>): Job
}