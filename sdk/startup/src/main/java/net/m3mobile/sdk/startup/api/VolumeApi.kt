package net.m3mobile.sdk.startup.api

import android.os.Build
import androidx.annotation.RequiresApi

interface VolumeApi {

    /**
     * Sets the media volume level.
     *
     * ### Range
     * The value must be between 0 and 15.
     * @param value The desired volume level for media.
     */
    fun setMediaVolume(value: Int)

    /**
     * Sets the ringtone volume level.
     *
     * ### Range
     * For models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `SL25`, and `PC10`,
     * it should be 0 to 15.
     *
     * For all others, it should be 0 to 7.
     * @param value The desired volume level for the ringtone.
     */
    fun setRingtoneVolume(value: Int)

    /**
     * Sets the notification volume level.
     *
     * ### Range
     * For models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, `SL25`, and `PC10`,
     * it should be 0 to 15.
     *
     * For all others, it should be 0 to 7.
     * @param value The desired volume level for notifications.
     */
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun setNotificationVolume(value: Int)

    /**
     * Sets the alarm volume level.
     *
     * ### Range
     * For models `SL10`, `SL10K`, `SL20`, `SL20K`, `SL20P`, and `PC10`,
     * it should be 0 to 15.
     *
     * For all others, it should be 0 to 7.
     * @param value The desired volume level for alarms.
     */
    fun setAlarmVolume(value: Int)

    /**
     * Enables the vibration mode.
     *
     * When vibration mode is enabled,
     * the ringtone volume and notification volume are automatically set to 0.
     */
    fun enableVibrationMode()

    /**
     * Disables the vibration mode.
     */
    fun disableVibrationMode()
}