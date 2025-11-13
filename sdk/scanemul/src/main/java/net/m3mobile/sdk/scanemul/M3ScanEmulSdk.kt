package net.m3mobile.sdk.scanemul

import android.content.Context

@Deprecated(
    message = "This interface is not intended for public use. Use M3ScanEmul.instance directly.",
    level = DeprecationLevel.HIDDEN
)
interface M3ScanEmulSdk

@Suppress("DEPRECATION_ERROR")
internal class M3ScanEmulSdkImpl(context: Context) : M3ScanEmulSdk