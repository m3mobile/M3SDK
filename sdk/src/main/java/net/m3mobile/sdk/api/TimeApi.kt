package net.m3mobile.sdk.api

import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

public interface TimeApi {

    /**
     * Retrieves the currently configured NTP server address.
     *
     * @return The NTP server address
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    public fun getNtpServer(): String

    /**
     * Retrieves the currently configured NTP synchronization interval in milliseconds.
     *
     * @return The NTP synchronization interval in milliseconds
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    public fun getNtpInterval(): Int

    /**
     * Retrieves the system's current default timezone.
     *
     * @return The timezone identifier
     */
    public fun getTimeZone(): String
}