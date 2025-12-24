package net.m3mobile.feature.scanemul.api

import androidx.annotation.IntRange
import net.m3mobile.core.RequiresScanEmul
import net.m3mobile.feature.scanemul.params.ScanSound

public interface ScanEmulScannerSettingApi {

    /**
     * Sets the sound feedback for a scan.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param sound The [ScanSound] enum to set as the feedback sound.
     * - [ScanSound.NONE]
     * - [ScanSound.BEEP]
     * - [ScanSound.DING_DONG]
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanSound(sound: ScanSound)

    /**
     * Enables vibration feedback for a scan.
     *
     * This only affects the scanner profile currently in operation.
     */
    @RequiresScanEmul("2.11.0")
    public fun enableScanVibration()

    /**
     * Disables vibration feedback for a scan.
     *
     * This only affects the scanner profile currently in operation.
     */
    @RequiresScanEmul("2.11.0")
    public fun disableScanVibration()

    /**
     * Enables LED feedback for a scan.
     *
     * This only affects the scanner profile currently in operation.
     */
    @RequiresScanEmul("2.11.0")
    public fun enableScanLed()

    /**
     * Disables LED feedback for a scan.
     *
     * This only affects the scanner profile currently in operation.
     */
    @RequiresScanEmul("2.11.0")
    public fun disableScanLed()

    /**
     * Sets the duration of the LED feedback for a scan.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param timeMillis The duration in milliseconds. The value must be between 1 and 1000.
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanLedTime(@IntRange(from = 1, to = 1000) timeMillis: Int)
}