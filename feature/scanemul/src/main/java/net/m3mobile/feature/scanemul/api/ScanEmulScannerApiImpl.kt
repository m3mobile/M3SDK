package net.m3mobile.feature.scanemul.api

import android.content.Context
import net.m3mobile.feature.scanemul.requester.CancelScanRequester
import net.m3mobile.feature.scanemul.requester.StartScanRequester

internal class ScanEmulScannerApiImpl(private val context: Context) : ScanEmulScannerApi {

    override fun startScan() {
        StartScanRequester(context).request()
    }

    override fun cancelScan() {
        CancelScanRequester(context).request()
    }
}