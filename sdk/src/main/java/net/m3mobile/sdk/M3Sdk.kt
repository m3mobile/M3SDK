@file:Suppress("DEPRECATION_ERROR")
package net.m3mobile.sdk

import android.content.Context
import net.m3mobile.core.InternalM3Api
import net.m3mobile.feature.keytool.M3KeyToolSdk
import net.m3mobile.feature.keytool.M3KeyToolSdkImpl
import net.m3mobile.feature.scanemul.M3ScanEmulSdk
import net.m3mobile.feature.scanemul.M3ScanEmulSdkImpl
import net.m3mobile.feature.startup.M3StartUpSdk
import net.m3mobile.feature.startup.M3StartUpSdkImpl

@Deprecated(
    message = "This interface is not intended for public use. Use M3Mobile.instance directly.",
    level = DeprecationLevel.HIDDEN
)
@InternalM3Api
public interface M3Sdk :
    M3StartUpSdk,
    M3ScanEmulSdk,
    M3KeyToolSdk

internal class M3SdkImpl(context: Context) : M3Sdk,
    M3StartUpSdk by M3StartUpSdkImpl(context),
    M3KeyToolSdk by M3KeyToolSdkImpl(context),
    M3ScanEmulSdk by M3ScanEmulSdkImpl(context)
