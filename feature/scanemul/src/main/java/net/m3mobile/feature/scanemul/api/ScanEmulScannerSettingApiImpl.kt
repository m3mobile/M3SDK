package net.m3mobile.feature.scanemul.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.utils.launchOnMain
import net.m3mobile.feature.scanemul.params.EndCharacter
import net.m3mobile.feature.scanemul.params.OutputMode
import net.m3mobile.feature.scanemul.params.ReadMode
import net.m3mobile.feature.scanemul.params.ScanSound
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScanResultPostfixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.GetScanResultPrefixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanLedTimeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultEndCharacterRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultPostfixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultPrefixRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanSoundRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScannerOutputModeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScannerReadModeRequester

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

    override fun setScannerOutputMode(mode: OutputMode) {
        SetScannerOutputModeRequester(context, mode).request()
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
}