package net.m3mobile.sdk.scanemul.api

import android.content.Context
import net.m3mobile.sdk.scanemul.requester.CancelScanRequester
import net.m3mobile.sdk.scanemul.requester.StartScanRequester

class ScannerApiImpl(private val context: Context) : ScannerApi {

    override fun startScan() {
        StartScanRequester(context).runBroadcast()
    }

    override fun cancelScan() {
        CancelScanRequester(context).runBroadcast()
    }
}