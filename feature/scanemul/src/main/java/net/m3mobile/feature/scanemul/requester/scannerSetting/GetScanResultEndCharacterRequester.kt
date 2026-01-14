package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import android.content.Intent
import net.m3mobile.core.requester.AwaitableBroadcastRequester
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.ResponseAction
import net.m3mobile.feature.scanemul.params.EndCharacter

internal class GetScanResultEndCharacterRequester(override val context: Context): AwaitableBroadcastRequester<EndCharacter>() {

    override val requestAction = RequestAction.GET_SCANNER_SETTING
    override val responseAction = ResponseAction.GET_SCANNER_SETTING

    override fun getExtra(intent: Intent): EndCharacter? {
        return EndCharacter.values().find { it.value == intent.getIntExtra("m3scanner_endchar", 0) }
    }
}