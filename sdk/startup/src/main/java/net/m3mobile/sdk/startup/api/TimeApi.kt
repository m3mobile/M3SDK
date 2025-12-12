package net.m3mobile.sdk.startup.api

import net.m3mobile.core.RequiresStartUp
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel
import java.time.LocalDateTime

public interface TimeApi {

    /**
     * Sets the date and time of the device.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    public fun setDateTime(dateTime: LocalDateTime)

    /**
     * Sets the NTP server for automatic time synchronization.
     *
     * Note that this setting will take effect from the next boot.
     *
     * StartUp version `6.4.9` or later is required.
     *
     * @param host The hostname or IP address of the NTP server.
     */
    @RequiresStartUp("6.4.9")
    public fun setNtpServer(host: String)

    /**
     * Sets the system's default timezone.
     *
     * StartUp version `6.5.9` or later is required.
     *
     * @param timezone The timezone identifier, e.g., "America/New_York".
     */
    @RequiresStartUp("6.5.9")
    public fun setTimeZone(timezone: String)


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