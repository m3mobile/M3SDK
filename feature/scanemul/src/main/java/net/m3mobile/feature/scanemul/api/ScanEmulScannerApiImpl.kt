package net.m3mobile.feature.scanemul.api

import android.content.Context
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.utils.launchOnMain
import net.m3mobile.feature.scanemul.listener.OnDigitalLinkParsedListener
import net.m3mobile.feature.scanemul.listener.OnGS1ParsedListener
import net.m3mobile.feature.scanemul.listener.OnScanResultListener
import net.m3mobile.feature.scanemul.requester.StartScanRequester
import net.m3mobile.feature.scanemul.requester.StopScanRequester
import net.m3mobile.feature.scanemul.requester.scanner.OnDecodeCompleteRequester
import net.m3mobile.feature.scanemul.requester.scanner.GetScannerStatusRequester
import net.m3mobile.feature.scanemul.requester.scanner.GetScannerTypeRequester
import java.util.concurrent.ConcurrentHashMap

internal class ScanEmulScannerApiImpl(private val context: Context) : ScanEmulScannerApi {

    private val onDecodeCompleteRequester by lazy {
        OnDecodeCompleteRequester(context).also {
            it.connect()
        }
    }
    private val scanResultListeners = ConcurrentHashMap<OnScanResultListener, Job>()
    private val gs1ParsedDataListeners = ConcurrentHashMap<OnGS1ParsedListener, Job>()
    private val digitalLinkParsedDataListeners = ConcurrentHashMap<OnDigitalLinkParsedListener, Job>()

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

    override fun registerOnScanResultListener(listener: OnScanResultListener) {
        scanResultListeners[listener]?.cancel()
        scanResultListeners[listener] = launchOnMain {
            onDecodeCompleteRequester.scanResult.collect {
                listener.onScanResult(it)
            }
        }
    }

    override fun unregisterOnScanResultListener(listener: OnScanResultListener) {
        scanResultListeners.remove(listener)?.cancel()
    }

    override fun registerOnGS1ParsedListener(listener: OnGS1ParsedListener) {
        gs1ParsedDataListeners[listener]?.cancel()
        gs1ParsedDataListeners[listener] = launchOnMain {
            onDecodeCompleteRequester.gs1ParsedResult.collect {
                listener.onGS1Parsed(it)
            }
        }
    }

    override fun unregisterOnGS1ParsedListener(listener: OnGS1ParsedListener) {
        gs1ParsedDataListeners.remove(listener)?.cancel()
    }

    override fun registerOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener) {
        digitalLinkParsedDataListeners[listener]?.cancel()
        digitalLinkParsedDataListeners[listener] = launchOnMain {
            onDecodeCompleteRequester.digitalLinkParsed.collect {
                listener.onDigitalLinkParsed(it)
            }
        }
    }

    override fun unregisterOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener) {
        digitalLinkParsedDataListeners.remove(listener)?.cancel()
    }
}