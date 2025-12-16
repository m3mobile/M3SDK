package net.m3mobile.feature.scanemul

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.scanemul.api.ScanEmulScannerApi
import net.m3mobile.feature.scanemul.api.ScanEmulScannerApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3ScanEmulSdk :
    ScanEmulScannerApi

@InternalM3Api
@Suppress("DEPRECATION_ERROR")
public class M3ScanEmulSdkImpl(context: Context) : M3ScanEmulSdk,
        ScanEmulScannerApi by ApiProxyFactory.create<ScanEmulScannerApi>(ScanEmulScannerApiImpl(context))