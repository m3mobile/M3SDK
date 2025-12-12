package net.m3mobile.sdk.scanemul.api

public interface ScannerApi {

    /**
     * Starts scan.
     */
    public fun startScan()

    /**
     * Cancels scan.
     */
    public fun cancelScan()
}