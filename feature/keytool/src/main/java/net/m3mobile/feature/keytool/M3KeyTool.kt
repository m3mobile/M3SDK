package net.m3mobile.feature.keytool

import android.content.Context

/**
 * Provides access to the M3 KeyTool SDK.
 * This serves as the main entry point for interacting with the M3 KeyTool settings.
 *
 * Use [M3KeyTool.instance] to get a concrete implementation of the [M3KeyToolKeyToolSdk] interface.
 */
@Suppress("DEPRECATION_ERROR")
public object M3KeyTool {

    /**
     * Provides access to the M3 KeyTool SDK.
     *
     * This is the main entry point for interacting with the M3 KeyTool settings
     * and is automatically initialized by the M3 library.
     */
    @JvmStatic
    public lateinit var instance: M3KeyToolKeyToolSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3KeyToolKeyToolSdkImpl(context)
    }
}