package net.m3mobile.feature.scanemul

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.scanemul.api.ScanEmulScannerApi
import net.m3mobile.feature.scanemul.api.ScanEmulScannerApiImpl
import net.m3mobile.feature.scanemul.api.ScanEmulScannerSettingApi
import net.m3mobile.feature.scanemul.api.ScanEmulScannerSettingApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3ScanEmulSdk :
    ScanEmulScannerApi,
    ScanEmulScannerSettingApi

@InternalM3Api
@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@Suppress("DEPRECATION_ERROR")
public class M3ScanEmulSdkImpl(context: Context) : M3ScanEmulSdk,
        ScanEmulScannerApi by ApiProxyFactory.create<ScanEmulScannerApi>(ScanEmulScannerApiImpl(context)),
        ScanEmulScannerSettingApi by ApiProxyFactory.create<ScanEmulScannerSettingApi>(
            ScanEmulScannerSettingApiImpl(context)
        )