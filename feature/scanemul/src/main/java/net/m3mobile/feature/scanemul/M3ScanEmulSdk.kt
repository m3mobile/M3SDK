package net.m3mobile.feature.scanemul

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.scanemul.api.ScannerApi
import net.m3mobile.feature.scanemul.api.ScannerApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3ScanEmulSdk :
    ScannerApi

@InternalM3Api
@Suppress("DEPRECATION_ERROR")
public class M3ScanEmulSdkImpl(context: Context) : M3ScanEmulSdk,
        ScannerApi by ApiProxyFactory.create<ScannerApi>(ScannerApiImpl(context))