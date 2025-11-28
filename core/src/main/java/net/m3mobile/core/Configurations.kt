package net.m3mobile.core

import android.content.Context
import android.content.pm.PackageManager
import kotlin.properties.Delegates

@InternalM3Api
object Configurations {

    var isStrictMode by Delegates.notNull<Boolean>()

    @JvmSynthetic
    internal fun Context.initIsStrictMode() {
        isStrictMode = try {
            val appInfo = packageManager.getApplicationInfo(
                packageName,
                PackageManager.GET_META_DATA
            )
            appInfo.metaData?.getBoolean("net.m3mobile.core.STRICT_MODE", false)!!
        } catch (_: Exception) {
            false
        }
    }
}