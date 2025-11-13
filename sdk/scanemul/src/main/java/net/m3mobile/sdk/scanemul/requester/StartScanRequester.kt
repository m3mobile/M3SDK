package net.m3mobile.sdk.scanemul.requester

import android.content.Context
import net.m3mobile.core.BroadcastRequester
import net.m3mobile.sdk.scanemul.constants.RequestAction

class StartScanRequester(override val context: Context) : BroadcastRequester() {
    override val requestAction = RequestAction.START_SCAN
}