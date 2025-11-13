package net.m3mobile.sdk.startup.requester.serial

import android.content.Context
import android.content.Intent
import net.m3mobile.core.AwaitableBroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.ResponseAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class GetSerialNumberRequester(
    override val context: Context
) : AwaitableBroadcastRequester<String>() {

    override val responseAction = ResponseAction.SYSTEM
    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.GET_SERIAL_NUMBER

    override fun getExtra(intent: Intent): String? {
        return intent.getStringExtra(typeValue)
    }
}