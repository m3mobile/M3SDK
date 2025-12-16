package net.m3mobile.feature.startup

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.startup.api.AirplaneModeApi
import net.m3mobile.feature.startup.api.AirplaneModeApiImpl
import net.m3mobile.feature.startup.api.AppApi
import net.m3mobile.feature.startup.api.AppApiImpl
import net.m3mobile.feature.startup.api.DeviceApi
import net.m3mobile.feature.startup.api.DeviceApiImpl
import net.m3mobile.feature.startup.api.NetworkApi
import net.m3mobile.feature.startup.api.NetworkApiImpl
import net.m3mobile.feature.startup.api.PermissionApi
import net.m3mobile.feature.startup.api.PermissionApiImpl
import net.m3mobile.feature.startup.api.QuickTileApi
import net.m3mobile.feature.startup.api.QuickTileApiImpl
import net.m3mobile.feature.startup.api.StartUpSettingApi
import net.m3mobile.feature.startup.api.StartUpSettingApiImpl
import net.m3mobile.feature.startup.api.TimeApi
import net.m3mobile.feature.startup.api.TimeApiImpl
import net.m3mobile.feature.startup.api.UsbApi
import net.m3mobile.feature.startup.api.UsbApiImpl
import net.m3mobile.feature.startup.api.WifiApi
import net.m3mobile.feature.startup.api.WifiApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
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

@InternalM3Api
@Suppress("DEPRECATION_ERROR")
public class M3StartUpSdkImpl(context: Context) : M3StartUpSdk,
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