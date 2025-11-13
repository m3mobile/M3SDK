package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import android.content.Intent
import net.m3mobile.core.AwaitableBroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.ResponseAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class GetWifiMacRequester(
    override val context: Context
) : AwaitableBroadcastRequester<String>() {

    override val requestAction = RequestAction.SYSTEM
    override val responseAction = ResponseAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.GET_WIFI_MAC

    override fun getExtra(intent: Intent): String? {
        return intent.getStringExtra(typeValue)
    }
}