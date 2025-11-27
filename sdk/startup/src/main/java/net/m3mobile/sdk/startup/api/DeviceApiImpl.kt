package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.params.DisplaySetting
import net.m3mobile.sdk.startup.requester.device.SetDisplaySettingRequester
import net.m3mobile.sdk.startup.requester.device.DisableVibrationModeRequester
import net.m3mobile.sdk.startup.requester.device.EnableVibrationModeRequester
import net.m3mobile.sdk.startup.requester.device.SetAlarmVolumeRequester
import net.m3mobile.sdk.startup.requester.device.SetMediaVolumeRequester
import net.m3mobile.sdk.startup.requester.device.SetNotificationVolumeRequester
import net.m3mobile.sdk.startup.requester.device.SetRingtoneVolumeRequester

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
}