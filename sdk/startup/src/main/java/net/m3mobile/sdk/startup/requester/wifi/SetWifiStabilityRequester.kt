package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class SetWifiStabilityRequester(): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_STABILITY
    override val extras
        get() = bundleOf("value" to value)
    protected abstract val value: Int
}

internal class SetWifiStabilityNormalRequester(override val context: Context): SetWifiStabilityRequester() {

    override val value = 1
}

internal class SetWifiStabilityHighRequester(override val context: Context): SetWifiStabilityRequester() {

    override val value = 2
}