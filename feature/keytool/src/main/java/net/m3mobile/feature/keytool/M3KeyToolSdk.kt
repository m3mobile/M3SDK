package net.m3mobile.feature.keytool

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.keytool.api.KeyApi
import net.m3mobile.feature.keytool.api.KeyApiImpl
import net.m3mobile.feature.keytool.api.WakeUpApi
import net.m3mobile.feature.keytool.api.WakeUpApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3KeyToolSdk :
    WakeUpApi,
    KeyApi

@InternalM3Api
@Suppress("DEPRECATION_ERROR")
public class M3KeyToolSdkImpl(context: Context): M3KeyToolSdk,
        WakeUpApi by ApiProxyFactory.create<WakeUpApi>(WakeUpApiImpl(context)),
        KeyApi by ApiProxyFactory.create<KeyApi>(KeyApiImpl(context))