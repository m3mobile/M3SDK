package net.m3mobile.sdk.scanemul

import android.content.Context
import net.m3mobile.core.device.DeviceSupportProxy
import net.m3mobile.sdk.scanemul.api.ScannerApi
import net.m3mobile.sdk.scanemul.api.ScannerApiImpl

@Deprecated(
    message = "This interface is not intended for public use. Use M3ScanEmul.instance directly.",
    level = DeprecationLevel.HIDDEN
)
interface M3ScanEmulSdk :
    ScannerApi

@Suppress("DEPRECATION_ERROR")
internal class M3ScanEmulSdkImpl(context: Context) : M3ScanEmulSdk,
        ScannerApi by DeviceSupportProxy.create<ScannerApi>(ScannerApiImpl(context))