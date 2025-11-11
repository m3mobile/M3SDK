package net.m3mobile.sdk.startup

import android.content.Context

/**
 * A singleton object that provides access to the M3Startup SDK.
 * This object serves as the main entry point for interacting with the M3 device's startup settings.
 *
 * Use [M3StartUp.instance] to get a concrete implementation of the [M3StartUpSdk] interface.
 */
@Suppress("DEPRECATION_ERROR")
object M3StartUp {

    @JvmStatic
    lateinit var instance: M3StartUpSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3StartUpSdkImpl(context)
    }
}