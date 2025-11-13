package net.m3mobile.sdk.keytool

import android.content.Context

@Suppress("DEPRECATION_ERROR")
object M3KeyTool {

    @JvmStatic
    lateinit var instance: M3KeyToolSdk
        private set

    @JvmSynthetic
    internal fun init(context: Context) {
        instance = M3KeyToolSdkImpl(context)
    }
}