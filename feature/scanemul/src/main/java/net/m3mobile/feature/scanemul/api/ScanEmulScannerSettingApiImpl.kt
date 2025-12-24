package net.m3mobile.feature.scanemul.api

import android.content.Context
import net.m3mobile.feature.scanemul.params.ScanSound
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.DisableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanLedRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.EnableScanVibrationRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanLedTimeRequester
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanSoundRequester

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
}