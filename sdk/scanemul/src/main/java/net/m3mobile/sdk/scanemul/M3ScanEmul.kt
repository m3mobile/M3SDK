package net.m3mobile.sdk.scanemul

import android.content.Context

/**
 * Provides access to the M3 ScanEmul SDK.
 * This serves as the main entry point for interacting with the M3 ScanEmul settings.
 *
 * Use [M3ScanEmul.instance] to get a concrete implementation of the [M3ScanEmulSdk] interface.
 */
@Suppress("DEPRECATION_ERROR")
public object M3ScanEmul {

    /**
     * Provides access to the M3 ScanEmul SDK.
     *
     * This is the main entry point for interacting with the M3 ScanEmul settings
     * and is automatically initialized by the M3 library.
     */
    @JvmStatic
    public lateinit var instance: M3ScanEmulSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3ScanEmulSdkImpl(context)
    }
}