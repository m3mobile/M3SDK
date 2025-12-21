package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal class ClearSavedWifiNetworksRequester(override val context: Context): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.CLEAR_WIFI
}