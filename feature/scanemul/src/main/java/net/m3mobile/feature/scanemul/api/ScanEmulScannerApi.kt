package net.m3mobile.feature.scanemul.api

public interface ScanEmulScannerApi {

    /**
     * Starts scan.
     */
    public fun startScan()

    /**
     * Cancels scan.
     */
    public fun cancelScan()
}