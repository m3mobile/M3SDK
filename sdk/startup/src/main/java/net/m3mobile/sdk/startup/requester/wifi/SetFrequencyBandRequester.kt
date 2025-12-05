package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.ExtraValue
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class SetFrequencyBandRequester: BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_FREQUENCY_BAND
    override val extras
        get() = bundleOf(ExtraKey.SET_FREQUENCY_BAND to value)
    protected abstract val value: Int
}

internal class AllowOnly5GHzFrequencyBandRequester(
    override val context: Context
): SetFrequencyBandRequester() {

    override val value = ExtraValue.ALLOW_ONLY_5G_FREQ_BAND
}

internal class AllowOnly2_4GHzFrequencyBandRequester(
    override val context: Context
): SetFrequencyBandRequester() {

    override val value = ExtraValue.ALLOW_ONLY_24G_FREQ_BAND
}

internal class AllowAllFrequencyBandRequester(
    override val context: Context
): SetFrequencyBandRequester() {

    override val value = ExtraValue.ALLOW_ALL_FREQ_BAND
}