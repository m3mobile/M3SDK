package net.m3mobile.feature.scanemul.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.RequiresScanEmul
import net.m3mobile.feature.scanemul.listener.OnDigitalLinkParsedListener
import net.m3mobile.feature.scanemul.listener.OnGS1ParsedListener
import net.m3mobile.feature.scanemul.listener.OnScanResultListener

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

    /**
     * Register a listener to be notified when a barcode is successfully scanned.
     *
     * @param listener The listener that will receive the scan results
     */
    public fun registerOnScanResultListener(listener: OnScanResultListener)

    /**
     * Unregister a previously registered listener to stop receiving scan results.
     *
     * @param listener The listener to be removed
     */
    public fun unregisterOnScanResultListener(listener: OnScanResultListener)

    /**
     * Registers a listener to be notified when a barcode is successfully scanned and parsed into GS1 format.
     *
     * @param listener The listener that will receive the GS1 parsed scan results
     */
    public fun registerOnGS1ParsedListener(listener: OnGS1ParsedListener)

    /**
     * Unregister a previously registered GS1 parsed listener to stop receiving GS1 scan results.
     *
     * @param listener The listener to be removed
     */
    public fun unregisterOnGS1ParsedListener(listener: OnGS1ParsedListener)

    /**
     * Registers a listener to be notified when a Digital Link is successfully parsed from a scanned barcode.
     *
     * @param listener The listener that will receive the parsed Digital Link results
     */
    public fun registerOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener)

    /**
     * Unregisters a previously registered listener to stop receiving parsed Digital Link scan results.
     *
     * @param listener The listener to be removed
     */
    public fun unregisterOnDigitalLinkParsedListener(listener: OnDigitalLinkParsedListener)
}