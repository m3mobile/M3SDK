package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.ExtraValue
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal abstract class SetWifiSleepPolicyRequester(): BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.WIFI_SLEEP
    override val extras
        get() = bundleOf(ExtraKey.SET_WIFI_SLEEP_POLICY to value)
    protected abstract val value: Int
}

internal class SetWifiSleepPolicyNeverRequester(override val context: Context): SetWifiSleepPolicyRequester() {
    override val value = ExtraValue.WIFI_SLEEP_NEVER
}

internal class SetWifiSleepPolicyPluggedOnlyRequester(override val context: Context): SetWifiSleepPolicyRequester() {
    override val value = ExtraValue.WIFI_SLEEP_PLUGGED_ONLY
}

internal class SetWifiSleepPolicyAlwaysRequester(override val context: Context): SetWifiSleepPolicyRequester() {
    override val value = ExtraValue.WIFI_SLEEP_ALWAYS
}