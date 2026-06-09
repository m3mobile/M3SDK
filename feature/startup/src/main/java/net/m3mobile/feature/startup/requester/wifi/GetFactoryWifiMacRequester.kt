package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import android.content.Intent
import net.m3mobile.core.requester.AwaitableBroadcastRequester
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.ResponseAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue
import net.m3mobile.feature.startup.params.FactoryWifiMacResult
import net.m3mobile.feature.startup.params.factoryWifiMacResult

internal class GetFactoryWifiMacRequester(
    override val context: Context
) : AwaitableBroadcastRequester<FactoryWifiMacResult>() {

    override val requestAction = RequestAction.SYSTEM
    override val responseAction = ResponseAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.GET_FACTORY_WIFI_MAC

    override fun getExtra(intent: Intent): FactoryWifiMacResult? {
        if (!intent.hasExtra(TypeValue.GET_FACTORY_WIFI_MAC_SUCCESS)) {
            return null
        }

        return factoryWifiMacResult(
            macAddress = intent.getStringExtra(TypeValue.GET_FACTORY_WIFI_MAC),
            success = intent.getBooleanExtra(TypeValue.GET_FACTORY_WIFI_MAC_SUCCESS, false),
            errorMessage = intent.getStringExtra(TypeValue.GET_FACTORY_WIFI_MAC_ERROR_MESSAGE)
        )
    }
}
