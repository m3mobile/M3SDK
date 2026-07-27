package net.m3mobile.feature.scanemul.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.utils.launchOnMain
import net.m3mobile.feature.scanemul.params.EndCharacter
import net.m3mobile.feature.scanemul.params.OutputMode
import net.m3mobile.feature.scanemul.params.ReadMode
import net.m3mobile.feature.scanemul.params.ScanSound
import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions
import net.m3mobile.feature.scanemul.params.ScannerButtonUiResult
import net.m3mobile.feature.scanemul.params.ScannerButtonUiVerificationResult
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetIsProfileEnabledRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScanResultEndCharacterRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScanResultOutputModeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScanResultPostfixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScanResultPrefixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScannerReadModeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanLedTimeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultEndCharacterRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultPostfixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultPrefixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanSoundRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultOutputModeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScannerReadModeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.ScannerButtonUiRequester

internal class ScanEmulScannerSettingApiImpl(private val context: Context): ScanEmulScannerSettingApi {

    override fun setScanSound(sound: ScanSound) {
        SetScanSoundRequester(context, sound).request()
    }

    override fun enableScanVibration() {
        EnableScanVibrationRequester(context).request()
    }

    override fun disableScanVibration() {
        DisableScanVibrationRequester(context).request()
    }

    override fun enableScanLed() {
        EnableScanLedRequester(context).request()
    }

    override fun disableScanLed() {
        DisableScanLedRequester(context).request()
    }

    override fun setScanLedTime(timeMillis: Int) {
        SetScanLedTimeRequester(context, timeMillis).request()
    }

    override fun setScannerReadMode(mode: ReadMode) {
        SetScannerReadModeRequester(context, mode).request()
    }

    override fun setScanResultOutputMode(mode: OutputMode) {
        SetScanResultOutputModeRequester(context, mode).request()
    }

    override fun setScanResultEndCharacter(endCharacter: EndCharacter) {
        SetScanResultEndCharacterRequester(context, endCharacter).request()
    }

    override fun setScanResultPrefix(prefix: String) {
        SetScanResultPrefixRequester(context, prefix).request()
    }

    override fun setScanResultPostfix(postfix: String) {
        SetScanResultPostfixRequester(context, postfix).request()
    }

    override suspend fun getScanResultPrefix(): String {
        return GetScanResultPrefixRequester(context).fetch()
    }

    override fun getScanResultPrefix(callback: RequestCallback<String>): Job {
        return launchOnMain {
            try {
                callback.onComplete(getScanResultPrefix(), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun getScanResultPostfix(): String {
        return GetScanResultPostfixRequester(context).fetch()
    }

    override fun getScanResultPostfix(callback: RequestCallback<String>): Job {
        return launchOnMain {
            try {
                callback.onComplete(getScanResultPostfix(), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun getScanResultEndCharacter(): EndCharacter {
        return GetScanResultEndCharacterRequester(context).fetch()
    }

    override fun getScanResultEndCharacter(callback: RequestCallback<EndCharacter>): Job {
        return launchOnMain {
            try {
                callback.onComplete(getScanResultEndCharacter(), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun getScanResultOutputMode(): OutputMode {
        return GetScanResultOutputModeRequester(context).fetch()
    }

    override fun getScanResultOutputMode(callback: RequestCallback<OutputMode>): Job {
        return launchOnMain {
            try {
                callback.onComplete(getScanResultOutputMode(), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun isScannerProfileEnabled(): Boolean {
        return GetIsProfileEnabledRequester(context).fetch()
    }

    override fun isScannerProfileEnabled(callback: RequestCallback<Boolean>): Job {
        return launchOnMain {
            try {
                callback.onComplete(isScannerProfileEnabled(), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun getScannerReadMode(): ReadMode {
        return GetScannerReadModeRequester(context).fetch()
    }

    override fun getScannerReadMode(callback: RequestCallback<ReadMode>): Job {
        return launchOnMain {
            try {
                callback.onComplete(getScannerReadMode(), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
    ): ScannerButtonUiResult =
        setScannerButtonUi(options, ScannerButtonUiRequester.requestId())

    override suspend fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
    ): ScannerButtonUiResult =
        ScannerButtonUiRequester(context, requestId, options).fetch()

    override fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job =
        setScannerButtonUi(options, ScannerButtonUiRequester.requestId(), callback)

    override fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job {
        return launchOnMain {
            try {
                callback.onComplete(setScannerButtonUi(options, requestId), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun getScannerButtonUi(): ScannerButtonUiResult =
        getScannerButtonUi(ScannerButtonUiRequester.requestId())

    override suspend fun getScannerButtonUi(requestId: String): ScannerButtonUiResult =
        ScannerButtonUiRequester(context, requestId, null).fetch()

    override fun getScannerButtonUi(
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job =
        getScannerButtonUi(ScannerButtonUiRequester.requestId(), callback)

    override fun getScannerButtonUi(
        requestId: String,
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job {
        return launchOnMain {
            try {
                callback.onComplete(getScannerButtonUi(requestId), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override suspend fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
    ): ScannerButtonUiVerificationResult =
        setAndVerifyScannerButtonUi(options, ScannerButtonUiRequester.requestId())

    override suspend fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
    ): ScannerButtonUiVerificationResult {
        val setResult = setScannerButtonUi(options, requestId)
        val getResult = if (setResult.saved()) {
            getScannerButtonUi(ScannerButtonUiRequester.requestId())
        } else {
            null
        }
        return ScannerButtonUiVerificationResult(setResult, getResult)
    }

    override fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
        callback: RequestCallback<ScannerButtonUiVerificationResult>,
    ): Job =
        setAndVerifyScannerButtonUi(options, ScannerButtonUiRequester.requestId(), callback)

    override fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
        callback: RequestCallback<ScannerButtonUiVerificationResult>,
    ): Job {
        return launchOnMain {
            try {
                callback.onComplete(setAndVerifyScannerButtonUi(options, requestId), null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }
}
