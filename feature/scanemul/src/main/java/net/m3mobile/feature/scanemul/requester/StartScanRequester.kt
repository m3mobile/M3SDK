package net.m3mobile.feature.scanemul.requester

import android.content.Context
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.scanemul.constants.RequestAction

internal class StartScanRequester(override val context: Context) : BroadcastRequester() {
    override val requestAction = RequestAction.START_SCAN
}