package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import android.content.Intent
import net.m3mobile.core.requester.AwaitableBroadcastRequester
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.ResponseAction

internal class GetScanResultPostfixRequester(override val context: Context): AwaitableBroadcastRequester<String>() {

    override val requestAction = RequestAction.GET_SCANNER_SETTING
    override val responseAction = ResponseAction.GET_SCANNER_SETTING

    override fun getExtra(intent: Intent): String? {
        return intent.getStringExtra("m3scanner_postfix")
    }
}