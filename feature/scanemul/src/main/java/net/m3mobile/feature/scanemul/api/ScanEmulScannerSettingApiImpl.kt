package net.m3mobile.feature.scanemul.api

import android.content.Context
import net.m3mobile.feature.scanemul.params.EndCharacter
import net.m3mobile.feature.scanemul.params.OutputMode
import net.m3mobile.feature.scanemul.params.ReadMode
import net.m3mobile.feature.scanemul.params.ScanSound
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanLedTimeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanResultEndCharacterRequester
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
}