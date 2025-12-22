package net.m3mobile.feature.scanemul.requester.scanner

import android.content.Context
import android.content.Intent
import net.m3mobile.core.requester.AwaitableBroadcastRequester
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.ResponseAction

internal abstract class GetScannerModuleRequester<T: Any>: AwaitableBroadcastRequester<T>() {

    override val requestAction = RequestAction.GET_SCANNER_MODULE
    override val responseAction = ResponseAction.GET_SCANNER_MODULE
}

internal class GetScannerTypeRequester(
    override val context: Context
): GetScannerModuleRequester<String>() {

    override fun getExtra(intent: Intent): String {
        return intent.getStringExtra("m3scanner_module_type") ?: ""
    }
}

internal class GetScannerStatusRequester(
    override val context: Context
): GetScannerModuleRequester<Int>() {

    override fun getExtra(intent: Intent): Int {
        return intent.getIntExtra("scanemul.extra.status", -1)
    }
}