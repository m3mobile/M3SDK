package net.m3mobile.sdk.startup.requester.device

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class VolumeRequester: BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.VOLUME
}

internal class DisableVibrationModeRequester(override val context: Context): VolumeRequester() {

    override val extras = bundleOf(ExtraKey.ENABLE_VOLUME_VIBRATOR to false)
}

internal class EnableVibrationModeRequester(override val context: Context): VolumeRequester() {

    override val extras = bundleOf(ExtraKey.ENABLE_VOLUME_VIBRATOR to true)
}

internal class SetAlarmVolumeRequester(
    override val context: Context,
    value: Int
): VolumeRequester() {

    override val extras = bundleOf(ExtraKey.SET_VOLUME_ALARM to value)
}

internal class SetMediaVolumeRequester(
    override val context: Context,
    value: Int
): VolumeRequester() {

    override val extras = bundleOf(ExtraKey.SET_VOLUME_MEDIA to value)
}

internal class SetNotificationVolumeRequester(
    override val context: Context,
    value: Int
): VolumeRequester() {

    override val extras = bundleOf(ExtraKey.SET_VOLUME_NOTIFICATION to value)
}

internal class SetRingtoneVolumeRequester(
    override val context: Context,
    value: Int
): VolumeRequester() {

    override val extras = bundleOf(ExtraKey.SET_VOLUME_RINGTONE to value)
}