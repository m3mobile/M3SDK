package net.m3mobile.sdk.keytool.requester.wakeup

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.BroadcastRequester
import net.m3mobile.sdk.keytool.constants.ExtraKey
import net.m3mobile.sdk.keytool.constants.RequestAction

internal class EnableRightScanWakeUpRequester(override val context: Context) : BroadcastRequester() {
    override val requestAction = RequestAction.RIGHT_SCAN_WAKEUP
    override val extras = bundleOf(
        ExtraKey.ENABLE_WAKEUP to true
    )
}