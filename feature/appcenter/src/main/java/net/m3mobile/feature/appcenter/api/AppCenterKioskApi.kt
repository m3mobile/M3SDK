package net.m3mobile.feature.appcenter.api

public interface AppCenterKioskApi {

    /**
     * Changes the AppCenter kiosk administrator password.
     *
     * The request is sent as a one-way explicit broadcast to AppCenter. A normal return only means
     * that Android accepted the broadcast; it does not confirm that AppCenter changed the password.
     *
     * @param currentPassword current kiosk administrator password. Empty strings are rejected.
     * @param newPassword new kiosk administrator password. Length must be from 4 to 20 characters.
     * @throws IllegalArgumentException if either password argument violates SDK input policy.
     */
    public fun changeKioskAdminPassword(currentPassword: String, newPassword: String)

    /**
     * Sets whether AppCenter keeps administrator mode while the screen is off.
     *
     * `true` keeps administrator mode after screen off. `false` restores the normal user-mode
     * behavior and may require administrator login again.
     *
     * @param enabled whether administrator mode should be kept while the screen is off.
     */
    public fun setKeepAdminModeOnSleep(enabled: Boolean)
}
