package net.m3mobile.sdk.startup

import android.content.Context
import net.m3mobile.core.device.DeviceSupportProxy
import net.m3mobile.sdk.startup.api.*

@Deprecated(
    message = "This interface is not intended for public use. Use M3StartUp.instance directly.",
    level = DeprecationLevel.HIDDEN
)
interface M3StartUpSdk :
    SerialApi,
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
        SerialApi by DeviceSupportProxy.create<SerialApi>(SerialApiImpl(context)),
        WifiApi by DeviceSupportProxy.create<WifiApi>(WifiApiImpl(context)),
        AirplaneModeApi by DeviceSupportProxy.create<AirplaneModeApi>(AirplaneModeApiImpl(context)),
        AppApi by DeviceSupportProxy.create<AppApi>(AppApiImpl(context)),
        PermissionApi by DeviceSupportProxy.create<PermissionApi>(PermissionApiImpl(context)),
        TimeApi by DeviceSupportProxy.create<TimeApi>(TimeApiImpl(context)),
        UsbApi by DeviceSupportProxy.create<UsbApi>(UsbApiImpl(context)),
        DeviceApi by DeviceSupportProxy.create<DeviceApi>(DeviceApiImpl(context)),
        NetworkApi by DeviceSupportProxy.create<NetworkApi>(NetworkApiImpl(context)),
        QuickTileApi by DeviceSupportProxy.create<QuickTileApi>(QuickTileApiImpl(context)),
        StartUpSettingApi by DeviceSupportProxy.create<StartUpSettingApi>(StartUpSettingApiImpl(context))