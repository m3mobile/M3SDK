package net.m3mobile.sdk.scanemul.api

import android.content.Context
import net.m3mobile.sdk.scanemul.requester.CancelScanRequester
import net.m3mobile.sdk.scanemul.requester.StartScanRequester

class ScannerApiImpl(private val context: Context) : ScannerApi {

    override fun startScan() {
        StartScanRequester(context).request()
    }

    override fun cancelScan() {
        CancelScanRequester(context).request()
    }
}