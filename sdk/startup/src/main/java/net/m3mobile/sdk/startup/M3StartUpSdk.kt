package net.m3mobile.sdk.startup

import android.content.Context
import net.m3mobile.core.device.DeviceSupportProxy
import net.m3mobile.sdk.startup.api.AirplaneModeApi
import net.m3mobile.sdk.startup.api.AirplaneModeApiImpl
import net.m3mobile.sdk.startup.api.AppApi
import net.m3mobile.sdk.startup.api.AppApiImpl
import net.m3mobile.sdk.startup.api.NetworkApi
import net.m3mobile.sdk.startup.api.NetworkApiImpl
import net.m3mobile.sdk.startup.api.PermissionApi
import net.m3mobile.sdk.startup.api.PermissionApiImpl
import net.m3mobile.sdk.startup.api.SerialApi
import net.m3mobile.sdk.startup.api.SerialApiImpl
import net.m3mobile.sdk.startup.api.TimeApi
import net.m3mobile.sdk.startup.api.TimeApiImpl
import net.m3mobile.sdk.startup.api.UsbApi
import net.m3mobile.sdk.startup.api.UsbApiImpl
import net.m3mobile.sdk.startup.api.DeviceApi
import net.m3mobile.sdk.startup.api.DeviceApiImpl
import net.m3mobile.sdk.startup.api.QuickTileApi
import net.m3mobile.sdk.startup.api.QuickTileApiImpl
import net.m3mobile.sdk.startup.api.WifiApi
import net.m3mobile.sdk.startup.api.WifiApiImpl

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
    QuickTileApi

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
        QuickTileApi by DeviceSupportProxy.create<QuickTileApi>(QuickTileApiImpl(context))