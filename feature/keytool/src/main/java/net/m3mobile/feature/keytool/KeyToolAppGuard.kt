package net.m3mobile.feature.keytool

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

private const val KEYTOOL_SL20_APP_NAME = "KeyTool SL20"
private const val KEYTOOL_SL20_PACKAGE = "com.m3.keytoolsl20"
private const val KEYTOOL_WAKE_UP_APP_NAME = "KeyTool"
private const val KEYTOOL_WAKE_UP_PACKAGE = "net.m3.keytool"

internal class KeyToolAppGuard(private val context: Context) {

    fun assertSl20AppAvailable(methodName: String) {
        assertAppAvailable(methodName, KEYTOOL_SL20_APP_NAME, KEYTOOL_SL20_PACKAGE)
    }

    fun assertWakeUpAppAvailable(methodName: String) {
        assertAppAvailable(methodName, KEYTOOL_WAKE_UP_APP_NAME, KEYTOOL_WAKE_UP_PACKAGE)
    }

    private fun assertAppAvailable(methodName: String, appName: String, packageName: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(packageName, 0)
            }
        } catch (_: PackageManager.NameNotFoundException) {
            throw KeyToolAppUnavailableException(methodName, appName, packageName)
        }
    }
}
