package net.m3mobile.feature.scanemul

import android.content.Context
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.scanemul.api.ScannerApi
import net.m3mobile.feature.scanemul.api.ScannerApiImpl

@Deprecated(
    message = "This interface is not intended for public use. Use M3ScanEmul.instance directly.",
    level = DeprecationLevel.HIDDEN
)
public interface M3ScanEmulSdk :
    ScannerApi

@Suppress("DEPRECATION_ERROR")
internal class M3ScanEmulSdkImpl(context: Context) : M3ScanEmulSdk,
        ScannerApi by ApiProxyFactory.create<ScannerApi>(ScannerApiImpl(context))