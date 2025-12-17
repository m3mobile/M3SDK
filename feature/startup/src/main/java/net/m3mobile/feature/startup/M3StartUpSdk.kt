package net.m3mobile.feature.startup

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.startup.api.StartUpAirplaneModeApi
import net.m3mobile.feature.startup.api.StartUpAirplaneModeApiImpl
import net.m3mobile.feature.startup.api.StartUpAppApi
import net.m3mobile.feature.startup.api.StartUpAppApiImpl
import net.m3mobile.feature.startup.api.StartUpDeviceApi
import net.m3mobile.feature.startup.api.StartUpDeviceApiImpl
import net.m3mobile.feature.startup.api.StartUpNetworkApi
import net.m3mobile.feature.startup.api.StartUpNetworkApiImpl
import net.m3mobile.feature.startup.api.StartUpPermissionApi
import net.m3mobile.feature.startup.api.StartUpPermissionApiImpl
import net.m3mobile.feature.startup.api.StartUpQuickTileApi
import net.m3mobile.feature.startup.api.StartUpQuickTileApiImpl
import net.m3mobile.feature.startup.api.StartUpSettingApi
import net.m3mobile.feature.startup.api.StartUpSettingApiImpl
import net.m3mobile.feature.startup.api.StartUpTimeApi
import net.m3mobile.feature.startup.api.StartUpTimeApiImpl
import net.m3mobile.feature.startup.api.StartUpUsbApi
import net.m3mobile.feature.startup.api.StartUpUsbApiImpl
import net.m3mobile.feature.startup.api.StartUpWifiApi
import net.m3mobile.feature.startup.api.StartUpWifiApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3StartUpSdk :
    StartUpWifiApi,
    StartUpAirplaneModeApi,
    StartUpAppApi,
    StartUpPermissionApi,
    StartUpTimeApi,
    StartUpUsbApi,
    StartUpDeviceApi,
    StartUpNetworkApi,
    StartUpQuickTileApi,
    StartUpSettingApi

@InternalM3Api
@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@Suppress("DEPRECATION_ERROR")
public class M3StartUpSdkImpl(context: Context) : M3StartUpSdk,
        StartUpWifiApi by ApiProxyFactory.create<StartUpWifiApi>(StartUpWifiApiImpl(context)),
        StartUpAirplaneModeApi by ApiProxyFactory.create<StartUpAirplaneModeApi>(StartUpAirplaneModeApiImpl(context)),
        StartUpAppApi by ApiProxyFactory.create<StartUpAppApi>(StartUpAppApiImpl(context)),
        StartUpPermissionApi by ApiProxyFactory.create<StartUpPermissionApi>(StartUpPermissionApiImpl(context)),
        StartUpTimeApi by ApiProxyFactory.create<StartUpTimeApi>(StartUpTimeApiImpl(context)),
        StartUpUsbApi by ApiProxyFactory.create<StartUpUsbApi>(StartUpUsbApiImpl(context)),
        StartUpDeviceApi by ApiProxyFactory.create<StartUpDeviceApi>(StartUpDeviceApiImpl(context)),
        StartUpNetworkApi by ApiProxyFactory.create<StartUpNetworkApi>(StartUpNetworkApiImpl(context)),
        StartUpQuickTileApi by ApiProxyFactory.create<StartUpQuickTileApi>(StartUpQuickTileApiImpl(context)),
        StartUpSettingApi by ApiProxyFactory.create<StartUpSettingApi>(StartUpSettingApiImpl(context))