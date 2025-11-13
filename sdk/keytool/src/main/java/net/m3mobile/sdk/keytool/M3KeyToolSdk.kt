package net.m3mobile.sdk.keytool

import android.content.Context

@Deprecated(
    message = "This interface is not intended for public use. Use M3KeyTool.instance directly.",
    level = DeprecationLevel.HIDDEN
)
interface M3KeyToolSdk

@Suppress("DEPRECATION_ERROR")
internal class M3KeyToolSdkImpl(context: Context): M3KeyToolSdk