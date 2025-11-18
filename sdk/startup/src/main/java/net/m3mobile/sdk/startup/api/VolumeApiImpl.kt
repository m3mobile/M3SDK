package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.requester.volume.DisableVibrationModeRequester
import net.m3mobile.sdk.startup.requester.volume.EnableVibrationModeRequester
import net.m3mobile.sdk.startup.requester.volume.SetAlarmVolumeRequester
import net.m3mobile.sdk.startup.requester.volume.SetMediaVolumeRequester
import net.m3mobile.sdk.startup.requester.volume.SetNotificationVolumeRequester
import net.m3mobile.sdk.startup.requester.volume.SetRingtoneVolumeRequester

internal class VolumeApiImpl(private val context: Context): VolumeApi {

    override fun setMediaVolume(value: Int) {
        SetMediaVolumeRequester(context, value).runBroadcast()
    }

    override fun setRingtoneVolume(value: Int) {
        SetRingtoneVolumeRequester(context, value).runBroadcast()
    }

    override fun setNotificationVolume(value: Int) {
        SetNotificationVolumeRequester(context, value).runBroadcast()
    }

    override fun setAlarmVolume(value: Int) {
        SetAlarmVolumeRequester(context, value).runBroadcast()
    }

    override fun enableVibrationMode() {
        EnableVibrationModeRequester(context).runBroadcast()
    }

    override fun disableVibrationMode() {
        DisableVibrationModeRequester(context).runBroadcast()
    }
}