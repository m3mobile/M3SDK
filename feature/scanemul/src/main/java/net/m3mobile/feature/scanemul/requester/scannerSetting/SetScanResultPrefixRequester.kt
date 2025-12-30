package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.scanemul.constants.ExtraKey
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.TypeKey
import net.m3mobile.feature.scanemul.constants.TypeValue

internal class SetScanResultPrefixRequester(
    override val context: Context,
    prefix: String
) : BroadcastRequester() {

    override val requestAction = RequestAction.SET_SCANNER_SETTING
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.SET_SCAN_RESULT_PREFIX
    override val extras = bundleOf(ExtraKey.PREFIX to prefix)
}