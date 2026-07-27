package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal class SetWifiEnabledRequester(
    override val context: Context,
    private val enabled: Boolean,
) : BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_ENABLED
    override val extras = bundleOf(ExtraKey.SET_WIFI_ENABLED to enabled)
}
