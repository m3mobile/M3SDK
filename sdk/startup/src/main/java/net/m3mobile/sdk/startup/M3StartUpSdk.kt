package net.m3mobile.sdk.startup

import android.content.Context
import net.m3mobile.core.device.DeviceSupportProxy
import net.m3mobile.sdk.startup.api.SerialApi
import net.m3mobile.sdk.startup.api.SerialApiImpl

@Deprecated(
    message = "This interface is not intended for public use. Use M3StartUp.instance directly.",
    level = DeprecationLevel.HIDDEN
)
interface M3StartUpSdk :
    SerialApi

@Suppress("DEPRECATION_ERROR")
internal class M3StartUpSdkImpl(context: Context) : M3StartUpSdk,
        SerialApi by DeviceSupportProxy.create<SerialApi>(SerialApiImpl(context))