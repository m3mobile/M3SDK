package net.m3mobile.sdk.startup.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.launchOnMain
import net.m3mobile.sdk.startup.params.DisplaySetting
import net.m3mobile.sdk.startup.requester.device.SetDisplaySettingRequester
import net.m3mobile.sdk.startup.requester.device.DisableVibrationModeRequester
import net.m3mobile.sdk.startup.requester.device.EnableVibrationModeRequester
import net.m3mobile.sdk.startup.requester.device.SetAlarmVolumeRequester
import net.m3mobile.sdk.startup.requester.device.SetMediaVolumeRequester
import net.m3mobile.sdk.startup.requester.device.SetNotificationVolumeRequester
import net.m3mobile.sdk.startup.requester.device.SetRingtoneVolumeRequester
import net.m3mobile.sdk.startup.requester.serial.GetSerialNumberRequester

internal class DeviceApiImpl(private val context: Context): DeviceApi {

    override fun setMediaVolume(value: Int) {
        SetMediaVolumeRequester(context, value).request()
    }

    override fun setRingtoneVolume(value: Int) {
        SetRingtoneVolumeRequester(context, value).request()
    }

    override fun setNotificationVolume(value: Int) {
        SetNotificationVolumeRequester(context, value).request()
    }

    override fun setAlarmVolume(value: Int) {
        SetAlarmVolumeRequester(context, value).request()
    }

    override fun enableVibrationMode() {
        EnableVibrationModeRequester(context).request()
    }

    override fun disableVibrationMode() {
        DisableVibrationModeRequester(context).request()
    }

    override fun setDisplaySetting(displaySetting: DisplaySetting) {
        SetDisplaySettingRequester(context, displaySetting).request()
    }

    override suspend fun getSerialNumber(): String {
        return GetSerialNumberRequester(context = context).fetch()
    }

    override fun getSerialNumber(callback: RequestCallback<String>): Job {
        return launchOnMain {
            try {
                val serial = getSerialNumber()
                callback.onComplete(serial, null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }
}