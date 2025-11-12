package net.m3mobile.sdk.startup

import android.content.Context

/**
 * Provides access to the M3 StartUp SDK.
 * This serves as the main entry point for interacting with the M3 StartUp settings.
 *
 * Use [M3StartUp.instance] to get a concrete implementation of the [M3StartUpSdk] interface.
 */
@Suppress("DEPRECATION_ERROR")
object M3StartUp {

    /**
     * Provides access to the M3 StartUp SDK.
     *
     * This is the main entry point for interacting with the M3 StartUp settings
     * and is automatically initialized by the M3 library.
     */
    @JvmStatic
    lateinit var instance: M3StartUpSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3StartUpSdkImpl(context)
    }
}