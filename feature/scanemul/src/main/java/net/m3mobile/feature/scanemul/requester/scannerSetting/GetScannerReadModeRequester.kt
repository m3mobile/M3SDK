package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import android.content.Intent
import net.m3mobile.core.requester.AwaitableBroadcastRequester
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.ResponseAction
import net.m3mobile.feature.scanemul.params.ReadMode

internal class GetScannerReadModeRequester(override val context: Context): AwaitableBroadcastRequester<ReadMode>() {

    override val requestAction = RequestAction.GET_SCANNER_SETTING
    override val responseAction = ResponseAction.GET_SCANNER_SETTING

    override fun getExtra(intent: Intent): ReadMode? {
        return ReadMode.values().find { it.value == intent.getIntExtra("m3scanner_read_mode", 0) }
    }
}