package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.scanemul.constants.ExtraKey
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.TypeKey
import net.m3mobile.feature.scanemul.constants.TypeValue

internal abstract class SetScanLedRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SET_SCANNER_SETTING
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.SET_SCAN_LED
    override val extras get() = bundleOf(ExtraKey.LED to if (enable) 1 else 0)
    protected abstract val enable: Boolean
}

internal class EnableScanLedRequester(override val context: Context): SetScanLedRequester() {

    override val enable = true
}

internal class DisableScanLedRequester(override val context: Context): SetScanLedRequester() {

    override val enable = false
}