package net.m3mobile.feature.scanemul.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.RequiresScanEmul

public interface ScanEmulScannerApi {

    /**
     * Starts scan.
     */
    @RequiresScanEmul("2.13.0")
    public fun startScan()

    /**
     * Cancels scan.
     */
    @RequiresScanEmul("2.13.0")
    public fun stopScan()

    /**
     * Retrieves device's scanner type.
     */
    @RequiresScanEmul("2.13.0")
    @JvmSynthetic
    public suspend fun getScannerType(): String

    /**
     * Retrieves device's scanner type by callback.
     */
    @RequiresScanEmul("2.13.0")
    public fun getScannerType(callback: RequestCallback<String>): Job

    /**
     * Retrieves device's current scanner status by callback.
     *
     * @return Return value indicates:
     * - `1`: Failed to open the scanner.
     * - `2`: Failed to close the scanner.
     * - `4`: Succeed to open the scanner.
     * - `8`: Succeed to close the scanner.
     */
    @RequiresScanEmul("2.13.0")
    @JvmSynthetic
    public suspend fun getScannerStatus(): Int

    /**
     * Retrieves device's current scanner status by callback.
     *
     * @param callback Return value indicates:
     * - `1`: Failed to open the scanner.
     * - `2`: Failed to close the scanner.
     * - `4`: Succeed to open the scanner.
     * - `8`: Succeed to close the scanner.
     */
    @RequiresScanEmul("2.13.0")
    public fun getScannerStatus(callback: RequestCallback<Int>): Job
}