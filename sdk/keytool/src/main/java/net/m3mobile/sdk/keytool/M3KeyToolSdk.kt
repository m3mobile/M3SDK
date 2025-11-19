package net.m3mobile.sdk.keytool

import android.content.Context
import net.m3mobile.core.device.DeviceSupportProxy
import net.m3mobile.sdk.keytool.api.KeyApi
import net.m3mobile.sdk.keytool.api.KeyApiImpl
import net.m3mobile.sdk.keytool.api.WakeUpApi
import net.m3mobile.sdk.keytool.api.WakeUpApiImpl

@Deprecated(
    message = "This interface is not intended for public use. Use M3KeyTool.instance directly.",
    level = DeprecationLevel.HIDDEN
)
interface M3KeyToolSdk :
    WakeUpApi,
    KeyApi

@Suppress("DEPRECATION_ERROR")
internal class M3KeyToolSdkImpl(context: Context): M3KeyToolSdk,
        WakeUpApi by DeviceSupportProxy.create<WakeUpApi>(WakeUpApiImpl(context)),
        KeyApi by DeviceSupportProxy.create<KeyApi>(KeyApiImpl(context))