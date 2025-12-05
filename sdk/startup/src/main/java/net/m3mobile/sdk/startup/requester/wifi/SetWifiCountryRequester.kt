package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class SetWifiCountryRequester(
    override val context: Context,
    code: String
): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_COUNTRY_CODE
    override val extras = bundleOf(ExtraKey.SET_WIFI_COUNTRY to code)
}