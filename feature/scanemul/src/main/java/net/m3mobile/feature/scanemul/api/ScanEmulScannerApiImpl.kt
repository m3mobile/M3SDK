package net.m3mobile.feature.scanemul.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.utils.launchOnMain
import net.m3mobile.feature.scanemul.requester.StopScanRequester
import net.m3mobile.feature.scanemul.requester.StartScanRequester
import net.m3mobile.feature.scanemul.requester.scanner.GetScannerStatusRequester
import net.m3mobile.feature.scanemul.requester.scanner.GetScannerTypeRequester

internal class ScanEmulScannerApiImpl(private val context: Context) : ScanEmulScannerApi {

    override fun startScan() {
        StartScanRequester(context).request()
    }

    override fun stopScan() {
        StopScanRequester(context).request()
    }

    override suspend fun getScannerType(): String {
        return GetScannerTypeRequester(context).fetch()
    }

    override fun getScannerType(callback: RequestCallback<String>): Job {
        return launchOnMain {
            try {
                val type = getScannerType()
                callback.onComplete(type, null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun getScannerStatus(): Int {
        return GetScannerStatusRequester(context).fetch()
    }

    override fun getScannerStatus(callback: RequestCallback<Int>): Job {
        return launchOnMain {
            try {
                val status = getScannerStatus()
                callback.onComplete(status, null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }
}