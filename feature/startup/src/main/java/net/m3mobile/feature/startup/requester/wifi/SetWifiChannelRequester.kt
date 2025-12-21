package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal class SetWifiChannelRequester(
    override val context: Context,
    channels: Array<out String>
): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_CHANNEL
    override val extras = bundleOf(ExtraKey.SET_WIFI_CHANNELS to channels)
}