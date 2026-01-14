package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.scanemul.constants.ExtraKey
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.TypeKey
import net.m3mobile.feature.scanemul.constants.TypeValue
import net.m3mobile.feature.scanemul.params.EndCharacter

internal class SetScanResultEndCharacterRequester(
    override val context: Context,
    endCharacter: EndCharacter
) : BroadcastRequester() {

    override val requestAction = RequestAction.SET_SCANNER_SETTING
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.SET_SCAN_RESULT_END_CHARACTER
    override val extras = bundleOf(ExtraKey.END_CHARACTER to endCharacter.value)
}