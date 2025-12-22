package net.m3mobile.feature.scanemul.api

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
}