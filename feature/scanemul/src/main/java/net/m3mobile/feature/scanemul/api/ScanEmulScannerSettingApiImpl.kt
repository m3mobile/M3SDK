package net.m3mobile.feature.scanemul.api

import android.content.Context
import net.m3mobile.feature.scanemul.params.ScanSound
import net.m3mobile.feature.scanemul.requester.scannerSetting.SetScanSoundRequester

internal class ScanEmulScannerSettingApiImpl(private val context: Context): ScanEmulScannerSettingApi {

    override fun setScanSound(sound: ScanSound) {
        SetScanSoundRequester(context, sound).request()
    }
}