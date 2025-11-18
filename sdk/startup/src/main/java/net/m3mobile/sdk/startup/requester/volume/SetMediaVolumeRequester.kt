package net.m3mobile.sdk.startup.requester.volume

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class SetMediaVolumeRequester(
    override val context: Context,
    value: Int
): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.VOLUME
    override val extras = bundleOf("volume_media" to value)
}