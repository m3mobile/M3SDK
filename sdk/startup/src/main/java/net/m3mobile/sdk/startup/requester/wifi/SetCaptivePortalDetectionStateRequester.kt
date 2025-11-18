package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class SetCaptivePortalDetectionStateRequester: BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.CAPTIVE_PORTAL
    override val extras = bundleOf("value" to value)
    protected abstract val value: Int
}

internal class EnableCaptivePortalDetectionRequester(override val context: Context): SetCaptivePortalDetectionStateRequester() {

    override val value = 1
}

internal class DisableCaptivePortalDetectionRequester(override val context: Context): SetCaptivePortalDetectionStateRequester() {

    override val value = 0
}