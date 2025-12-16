package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.ExtraValue
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal abstract class SetWifiStabilityRequester(): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_STABILITY
    override val extras
        get() = bundleOf(ExtraKey.SET_WIFI_STABILITY to value)
    protected abstract val value: Int
}

internal class SetWifiStabilityNormalRequester(override val context: Context): SetWifiStabilityRequester() {

    override val value = ExtraValue.WIFI_NORMAL_STABILITY
}

internal class SetWifiStabilityHighRequester(override val context: Context): SetWifiStabilityRequester() {

    override val value = ExtraValue.WIFI_HARD_STABILITY
}