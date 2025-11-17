package net.m3mobile.sdk.startup.api

import java.time.LocalDateTime

interface TimeApi {

    /**
     * Sets the date and time of the device.
     */
    fun setDateTime(dateTime: LocalDateTime)


    /**
     * Sets the NTP server for automatic time synchronization.
     *
     * @param host The hostname or IP address of the NTP server.
     */
    fun setNtpServer(host: String)

    /**
     * Sets the system's default timezone.
     *
     * @param timezone The timezone identifier, e.g., "America/New_York".
     */
    fun setTimezone(timezone: String)
}