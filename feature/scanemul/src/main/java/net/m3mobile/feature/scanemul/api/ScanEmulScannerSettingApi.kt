package net.m3mobile.feature.scanemul.api

import androidx.annotation.IntRange
import net.m3mobile.core.RequiresScanEmul
import net.m3mobile.feature.scanemul.params.EndCharacter
import net.m3mobile.feature.scanemul.params.OutputMode
import net.m3mobile.feature.scanemul.params.ReadMode
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

    /**
     * Sets the read mode of the scanner.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param mode The [ReadMode] to set for the scanner.
     * @see ReadMode
     */
    @RequiresScanEmul("2.11.0")
    public fun setScannerReadMode(mode: ReadMode)

    /**
     * Sets the output mode for the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param mode The [OutputMode] to set for the scanner's output.
     * @see OutputMode
     */
    @RequiresScanEmul("2.11.0")
    public fun setScannerOutputMode(mode: OutputMode)

    /**
     * Sets the end character to be appended to the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param endCharacter The [EndCharacter] to append to the scan result.
     * @see EndCharacter
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultEndCharacter(endCharacter: EndCharacter)

    /**
     * Sets a prefix to be added to the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param prefix The string to prepend to the scan result.
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultPrefix(prefix: String)

    /**
     * Sets a postfix to be added to the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param postfix The string to prepend to the scan result.
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultPostfix(postfix: String)
}