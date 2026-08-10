package net.m3mobile.feature.scanemul.api

import androidx.annotation.IntRange
import kotlinx.coroutines.Job
import net.m3mobile.core.ModelVersion
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.RequiresScanEmul
import net.m3mobile.core.UnsupportedModels
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.feature.scanemul.params.EndCharacter
import net.m3mobile.feature.scanemul.params.OutputMode
import net.m3mobile.feature.scanemul.params.ReadMode
import net.m3mobile.feature.scanemul.params.ScanSound
import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions
import net.m3mobile.feature.scanemul.params.ScannerButtonUiResult
import net.m3mobile.feature.scanemul.params.ScannerButtonUiVerificationResult

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

    /**
     * Checks if the current scanner profile is enabled.
     *
     * @return `true` if the profile is enabled, `false` otherwise
     */
    @JvmSynthetic
    @RequiresScanEmul("2.11.0")
    public suspend fun isScannerProfileEnabled(): Boolean

    /**
     * Checks if the current scanner profile is enabled.
     *
     * @param callback The callback to be invoked with the result
     */
    @RequiresScanEmul("2.11.0")
    public fun isScannerProfileEnabled(callback: RequestCallback<Boolean>): Job

    /**
     * Gets the read mode of the scanner in the current scanner profile.
     *
     * @return The current [ReadMode] of the scanner
     */
    @JvmSynthetic
    @RequiresScanEmul("2.11.0")
    public suspend fun getScannerReadMode(): ReadMode

    /**
     * Gets the read mode of the scanner in the current scanner profile.
     *
     * @param callback The callback to be invoked with the result
     */
    @RequiresScanEmul("2.11.0")
    public fun getScannerReadMode(callback: RequestCallback<ReadMode>): Job

    /**
     * Sets floating scanner button UI settings.
     *
     * Missing values are not sent to ScanEmul and keep their current values.
     * Empty image path means the default ScanEmul button image.
     *
     * ScanEmul version `4.15.1` or later is required. SM24 requires `4.14.10` or later.
     */
    @JvmSynthetic
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public suspend fun setScannerButtonUi(options: ScannerButtonUiOptions): ScannerButtonUiResult

    /**
     * Sets floating scanner button UI settings with a caller-provided request id.
     */
    @JvmSynthetic
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public suspend fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
    ): ScannerButtonUiResult

    /**
     * Sets floating scanner button UI settings by callback.
     */
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job

    /**
     * Sets floating scanner button UI settings by callback with a caller-provided request id.
     */
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public fun setScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job

    /**
     * Gets floating scanner button UI settings.
     */
    @JvmSynthetic
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public suspend fun getScannerButtonUi(): ScannerButtonUiResult

    /**
     * Gets floating scanner button UI settings with a caller-provided request id.
     */
    @JvmSynthetic
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public suspend fun getScannerButtonUi(requestId: String): ScannerButtonUiResult

    /**
     * Gets floating scanner button UI settings by callback.
     */
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public fun getScannerButtonUi(callback: RequestCallback<ScannerButtonUiResult>): Job

    /**
     * Gets floating scanner button UI settings by callback with a caller-provided request id.
     */
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public fun getScannerButtonUi(
        requestId: String,
        callback: RequestCallback<ScannerButtonUiResult>,
    ): Job

    /**
     * Sets floating scanner button UI settings and verifies saved values with a GET request
     * after the SET result reports a saved state.
     */
    @JvmSynthetic
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public suspend fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
    ): ScannerButtonUiVerificationResult

    /**
     * Sets floating scanner button UI settings with a caller-provided SET request id and
     * verifies saved values with a GET request after the SET result reports a saved state.
     */
    @JvmSynthetic
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public suspend fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
    ): ScannerButtonUiVerificationResult

    /**
     * Sets floating scanner button UI settings and verifies saved values by callback.
     */
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
        callback: RequestCallback<ScannerButtonUiVerificationResult>,
    ): Job

    /**
     * Sets floating scanner button UI settings with a caller-provided SET request id and
     * verifies saved values by callback.
     */
    @UnsupportedModels(DeviceModel.WD10)
    @RequiresScanEmul("4.15.1", ModelVersion(DeviceModel.SM24, "4.14.10"))
    public fun setAndVerifyScannerButtonUi(
        options: ScannerButtonUiOptions,
        requestId: String,
        callback: RequestCallback<ScannerButtonUiVerificationResult>,
    ): Job
}
