package net.m3mobile.feature.scanemul.api

import androidx.annotation.IntRange
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
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
     * @param sound The [ScanSound] enum to set as the feedback sound
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
     * @param timeMillis The duration in milliseconds. The value must be between 1 and 1000
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanLedTime(@IntRange(from = 1, to = 1000) timeMillis: Int)

    /**
     * Sets the read mode of the scanner.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param mode The [ReadMode] to set for the scanner
     * @see ReadMode
     */
    @RequiresScanEmul("2.11.0")
    public fun setScannerReadMode(mode: ReadMode)

    /**
     * Sets the output mode for the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param mode The [OutputMode] to set for the scanner's output
     * @see OutputMode
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultOutputMode(mode: OutputMode)

    /**
     * Sets the end character to be appended to the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param endCharacter The [EndCharacter] to append to the scan result
     * @see EndCharacter
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultEndCharacter(endCharacter: EndCharacter)

    /**
     * Sets a prefix to be added to the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param prefix The string to prepend to the scan result
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultPrefix(prefix: String)

    /**
     * Sets a postfix to be added to the scanned data.
     *
     * This only affects the scanner profile currently in operation.
     *
     * @param postfix The string to prepend to the scan result
     */
    @RequiresScanEmul("2.11.0")
    public fun setScanResultPostfix(postfix: String)

    /**
     * Gets the prefix for the scanned data in the current scanner profile.
     *
     * @return The prefix for the scanned data
     */
    @JvmSynthetic
    @RequiresScanEmul("2.11.0")
    public suspend fun getScanResultPrefix(): String

    /**
     * Gets the prefix for the scanned data in the current scanner profile.
     *
     * @param callback The callback to be invoked with the result
     */
    @RequiresScanEmul("2.11.0")
    public fun getScanResultPrefix(callback: RequestCallback<String>): Job

    /**
     * Gets the postfix for the scanned data in the current scanner profile.
     *
     * @return The postfix for the scanned data
     */
    @JvmSynthetic
    @RequiresScanEmul("2.11.0")
    public suspend fun getScanResultPostfix(): String

    /**
     * Gets the postfix for the scanned data in the current scanner profile.
     *
     * @param callback The callback to be invoked with the result
     */
    @RequiresScanEmul("2.11.0")
    public fun getScanResultPostfix(callback: RequestCallback<String>): Job

    /**
     * Gets the end character that is appended to the scanned data in the current scanner profile.
     *
     * @return The [EndCharacter] appended to the scan result
     */
    @JvmSynthetic
    @RequiresScanEmul("2.11.0")
    public suspend fun getScanResultEndCharacter(): EndCharacter

    /**
     * Gets the end character that is appended to the scanned data in the current scanner profile.
     *
     * @param callback The callback to be invoked with the result
     */
    @RequiresScanEmul("2.11.0")
    public fun getScanResultEndCharacter(callback: RequestCallback<EndCharacter>): Job

    /**
     * Gets the output mode for the scanned data in the current scanner profile.
     *
     * @return The current [OutputMode] of the scanner
     */
    @JvmSynthetic
    @RequiresScanEmul("2.11.0")
    public suspend fun getScanResultOutputMode(): OutputMode

    /**
     * Gets the output mode for the scanned data in the current scanner profile.
     *
     * @param callback The callback to be invoked with the result
     */
    @RequiresScanEmul("2.11.0")
    public fun getScanResultOutputMode(callback: RequestCallback<OutputMode>): Job
}