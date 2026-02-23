package net.m3mobile.feature.startup.requester.bluetooth

import android.content.Context
import android.content.Intent
import net.m3mobile.core.requester.AwaitableBroadcastRequester
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.ResponseAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal class GetBluetoothMacRequester(
    override val context: Context
) : AwaitableBroadcastRequester<String>() {

    override val requestAction = RequestAction.SYSTEM
    override val responseAction = ResponseAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.GET_BLUETOOTH_MAC

    override fun getExtra(intent: Intent): String? {
        return intent.getStringExtra(typeValue)
    }
}
