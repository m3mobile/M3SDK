package net.m3mobile.feature.startup

import android.content.Context
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.startup.api.*

@Deprecated(
    message = "This interface is not intended for public use. Use M3StartUp.instance directly.",
    level = DeprecationLevel.HIDDEN
)
public interface M3StartUpSdk :
    WifiApi,
    AirplaneModeApi,
    AppApi,
    PermissionApi,
    TimeApi,
    UsbApi,
    DeviceApi,
    NetworkApi,
    QuickTileApi,
    StartUpSettingApi

@Suppress("DEPRECATION_ERROR")
internal class M3StartUpSdkImpl(context: Context) : M3StartUpSdk,
        WifiApi by ApiProxyFactory.create<WifiApi>(WifiApiImpl(context)),
        AirplaneModeApi by ApiProxyFactory.create<AirplaneModeApi>(AirplaneModeApiImpl(context)),
        AppApi by ApiProxyFactory.create<AppApi>(AppApiImpl(context)),
        PermissionApi by ApiProxyFactory.create<PermissionApi>(PermissionApiImpl(context)),
        TimeApi by ApiProxyFactory.create<TimeApi>(TimeApiImpl(context)),
        UsbApi by ApiProxyFactory.create<UsbApi>(UsbApiImpl(context)),
        DeviceApi by ApiProxyFactory.create<DeviceApi>(DeviceApiImpl(context)),
        NetworkApi by ApiProxyFactory.create<NetworkApi>(NetworkApiImpl(context)),
        QuickTileApi by ApiProxyFactory.create<QuickTileApi>(QuickTileApiImpl(context)),
        StartUpSettingApi by ApiProxyFactory.create<StartUpSettingApi>(StartUpSettingApiImpl(context))