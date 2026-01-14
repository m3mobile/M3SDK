package net.m3mobile.feature.scanemul.listener

import net.m3mobile.feature.scanemul.params.ScanResult

/**
 * Interface definition for a callback to be invoked when a scan operation is completed.
 */
public fun interface OnScanResultListener {
    /**
     * Called when a scan operation has successfully completed or produced a result.
     *
     * @param result The [ScanResult] object containing the scanned data
     */
    public fun onScanResult(result: ScanResult)
}