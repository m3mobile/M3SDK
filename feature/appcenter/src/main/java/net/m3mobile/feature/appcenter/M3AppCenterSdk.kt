package net.m3mobile.feature.appcenter

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.feature.appcenter.api.AppCenterKioskApi
import net.m3mobile.feature.appcenter.api.AppCenterKioskApiImpl

@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3AppCenterSdk : AppCenterKioskApi

@InternalM3Api
@Deprecated(
    message = "This interface is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
@Suppress("DEPRECATION_ERROR")
public class M3AppCenterSdkImpl(context: Context) : M3AppCenterSdk,
        AppCenterKioskApi by AppCenterKioskApiImpl(context)
