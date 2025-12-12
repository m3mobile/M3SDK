package net.m3mobile.sdk.scanemul.requester

import android.content.Context
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.scanemul.constants.RequestAction

internal class CancelScanRequester(override val context: Context) : BroadcastRequester() {
    override val requestAction = RequestAction.CANCEL_SCAN
}