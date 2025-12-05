package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.ExtraValue
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class SetCaptivePortalDetectionStateRequester: BroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.CAPTIVE_PORTAL
    override val extras
        get() = bundleOf(ExtraKey.SET_CAPTIVE_PORTAL_DETECTION to value)
    protected abstract val value: Int
}

internal class EnableCaptivePortalDetectionRequester(override val context: Context): SetCaptivePortalDetectionStateRequester() {

    override val value = ExtraValue.ENABLE_CAPTIVE_PORTAL_DETECTION
}

internal class DisableCaptivePortalDetectionRequester(override val context: Context): SetCaptivePortalDetectionStateRequester() {

    override val value = ExtraValue.DISABLE_CAPTIVE_PORTAL_DETECTION
}