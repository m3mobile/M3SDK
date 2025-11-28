package net.m3mobile.sdk.startup.api

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.sdk.startup.params.DisplaySetting

interface DeviceApi {

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

    /**
     * Sets the device's display settings based on the provided configuration.
     *
     * @param displaySetting The [DisplaySetting] object containing the desired display configurations.
     */
    fun setDisplaySetting(displaySetting: DisplaySetting)

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