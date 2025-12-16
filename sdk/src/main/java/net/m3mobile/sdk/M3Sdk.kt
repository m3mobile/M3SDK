@file:Suppress("DEPRECATION_ERROR")
package net.m3mobile.sdk

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.feature.keytool.M3KeyToolKeyToolSdk
import net.m3mobile.feature.keytool.M3KeyToolKeyToolSdkImpl
import net.m3mobile.feature.scanemul.M3ScanEmulSdk
import net.m3mobile.feature.scanemul.M3ScanEmulSdkImpl
import net.m3mobile.feature.startup.M3StartUpSdk
import net.m3mobile.feature.startup.M3StartUpSdkImpl

@Deprecated(
    message = "This interface is not intended for public use. Use M3Mobile.instance directly.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3Sdk : M3StartUpSdk

internal class M3SdkImpl(context: Context) : M3Sdk,
    M3StartUpSdk by M3StartUpSdkImpl(context),
    M3KeyToolKeyToolSdk by M3KeyToolKeyToolSdkImpl(context),
    M3ScanEmulSdk by M3ScanEmulSdkImpl(context)