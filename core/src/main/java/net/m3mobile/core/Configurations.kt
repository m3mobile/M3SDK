@file:JvmSynthetic
package net.m3mobile.core

import android.content.Context
import android.content.pm.PackageManager

internal lateinit var appContext: Context

internal val IS_STRICT_MODE by lazy {
    with(appContext) {
        try {
            val appInfo = packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
            appInfo.metaData?.getBoolean("M3_STRICT_MODE", false)!!
        } catch (_: Exception) {
            false
        }
    }
}