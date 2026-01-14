package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.scanemul.constants.ExtraKey
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.TypeKey
import net.m3mobile.feature.scanemul.constants.TypeValue

internal abstract class SetScanVibrationRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SET_SCANNER_SETTING
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.SET_SCAN_VIBRATION
    override val extras get() = bundleOf(ExtraKey.VIBRATION to if(enable) 1 else 0)
    protected abstract val enable: Boolean
}

internal class EnableScanVibrationRequester(override val context: Context): SetScanVibrationRequester() {

    override val enable = true
}

internal class DisableScanVibrationRequester(override val context: Context): SetScanVibrationRequester() {

    override val enable = false
}