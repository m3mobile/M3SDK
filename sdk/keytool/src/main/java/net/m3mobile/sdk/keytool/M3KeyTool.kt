package net.m3mobile.sdk.keytool

import android.content.Context

/**
 * Provides access to the M3 KeyTool SDK.
 * This serves as the main entry point for interacting with the M3 KeyTool settings.
 *
 * Use [M3KeyTool.instance] to get a concrete implementation of the [M3KeyToolSdk] interface.
 */
@Suppress("DEPRECATION_ERROR")
object M3KeyTool {

    /**
     * Provides access to the M3 KeyTool SDK.
     *
     * This is the main entry point for interacting with the M3 KeyTool settings
     * and is automatically initialized by the M3 library.
     */
    @JvmStatic
    lateinit var instance: M3KeyToolSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3KeyToolSdkImpl(context)
    }
}