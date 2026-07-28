package net.m3mobile.feature.keytool

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import net.m3mobile.core.UnsatisfiedVersionException
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.feature.keytool.constants.KeyToolContract
import net.m3mobile.feature.keytool.internal.KeyToolVersionPolicy

internal class KeyToolAppGuard(private val context: Context) {

    fun assertSl20AppVersion(
        methodName: String,
        model: DeviceModel,
        requiredVersion: String,
    ) {
        val currentVersion = appVersion(
            methodName,
            KeyToolContract.SL20_APP_NAME,
            KeyToolContract.SL20_PACKAGE,
        )
        if (!KeyToolVersionPolicy.versionSatisfied(currentVersion, requiredVersion)) {
            throw UnsatisfiedVersionException(
                "\"$methodName\" is not available on $model because " +
                    "${KeyToolContract.SL20_APP_NAME} (${KeyToolContract.SL20_PACKAGE}) version " +
                    "'$currentVersion' is installed. Required version is '$requiredVersion'."
            )
        }
    }

    fun assertLegacyWakeUpAppAvailable(methodName: String) {
        appVersion(
            methodName,
            KeyToolContract.LEGACY_WAKE_UP_APP_NAME,
            KeyToolContract.LEGACY_WAKE_UP_PACKAGE,
        )
    }

    private fun appVersion(
        methodName: String,
        appName: String,
        packageName: String,
    ): String = try {
        val packageInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(packageName, 0)
            }
        packageInfo.versionName
            ?: throw KeyToolAppUnavailableException(methodName, appName, packageName)
    } catch (_: PackageManager.NameNotFoundException) {
        throw KeyToolAppUnavailableException(methodName, appName, packageName)
    }
}
