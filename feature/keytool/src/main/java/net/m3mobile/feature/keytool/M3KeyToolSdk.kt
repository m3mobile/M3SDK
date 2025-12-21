package net.m3mobile.feature.keytool

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.proxy.ApiProxyFactory
import net.m3mobile.feature.keytool.api.KeyToolKeyApi
import net.m3mobile.feature.keytool.api.KeyToolKeyApiImpl
import net.m3mobile.feature.keytool.api.KeyToolWakeUpApi
import net.m3mobile.feature.keytool.api.KeyToolWakeUpApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3KeyToolSdk :
    KeyToolWakeUpApi,
    KeyToolKeyApi

@InternalM3Api
@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@Suppress("DEPRECATION_ERROR")
public class M3KeyToolSdkImpl(context: Context): M3KeyToolSdk,
        KeyToolWakeUpApi by ApiProxyFactory.create<KeyToolWakeUpApi>(KeyToolWakeUpApiImpl(context)),
        KeyToolKeyApi by ApiProxyFactory.create<KeyToolKeyApi>(KeyToolKeyApiImpl(context))