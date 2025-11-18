package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class SetFrequencyBandRequester: BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_FREQUENCY_BAND
    override val extras = bundleOf("value" to value)
    protected abstract val value: Int
}

internal class AllowOnly5GHzFrequencyBandRequester(
    override val context: Context
): SetFrequencyBandRequester() {

    override val value = 2
}

internal class AllowOnly2_4GHzFrequencyBandRequester(
    override val context: Context
): SetFrequencyBandRequester() {

    override val value = 1
}

internal class AllowAllFrequencyBandRequester(
    override val context: Context
): SetFrequencyBandRequester() {

    override val value = 0
}