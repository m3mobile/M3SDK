package net.m3mobile.feature.startup

import android.content.Context

/**
 * Provides access to the M3 StartUp SDK.
 * This serves as the main entry point for interacting with the M3 StartUp settings.
 *
 * Use [M3StartUp.instance] to get a concrete implementation of the [M3StartUpSdk] interface.
 */
@Suppress("DEPRECATION_ERROR")
@Deprecated(
    message = "Use \"M3Mobile.instance\" instead.",
    level = DeprecationLevel.WARNING
)
public object M3StartUp {

    /**
     * Provides access to the M3 StartUp SDK.
     *
     * This is the main entry point for interacting with the M3 StartUp settings
     * and is automatically initialized by the M3 library.
     */
    @JvmStatic
    public lateinit var instance: M3StartUpSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3StartUpSdkImpl(context)
    }
}