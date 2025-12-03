package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class RemoveWifiNetworkRequester(
    override val context: Context,
    ssid: String
) : BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.REMOVE_WIFI
    override val extras = bundleOf("ssid" to ssid)
}