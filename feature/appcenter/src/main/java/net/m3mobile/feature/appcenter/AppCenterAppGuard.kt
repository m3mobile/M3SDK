package net.m3mobile.feature.appcenter

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import net.m3mobile.core.UnsatisfiedVersionException
import net.m3mobile.feature.appcenter.internal.AppCenterContract

internal fun interface AppCenterAvailability {
    fun assertAppAvailable(methodName: String)
}

internal class AppCenterAppGuard(context: Context) : AppCenterAvailability {

    private val context = context.applicationContext ?: context

    override fun assertAppAvailable(methodName: String) {
        val versionName = appVersionName() ?: throw UnsatisfiedVersionException(
            "\"$methodName\" is not available because ${AppCenterContract.APP_NAME} is not installed. " +
                "Required ${AppCenterContract.APP_NAME} version is '${AppCenterContract.REQUIRED_VERSION}'."
        )

        if (!versionName.versionAtLeast(AppCenterContract.REQUIRED_VERSION)) {
            throw UnsatisfiedVersionException(
                "\"$methodName\" is not available on the current ${AppCenterContract.APP_NAME} " +
                    "version '$versionName'. Required ${AppCenterContract.APP_NAME} version is " +
                    "'${AppCenterContract.REQUIRED_VERSION}'."
            )
        }
    }

    private fun appVersionName(): String? =
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    AppCenterContract.APP_PACKAGE,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(AppCenterContract.APP_PACKAGE, 0)
            }
            packageInfo.versionName
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }

    private fun String.versionAtLeast(required: String): Boolean {
        val current = versionParts()
        val target = required.versionParts()

        current.zip(target).forEach { (currentPart, targetPart) ->
            if (currentPart != targetPart) return currentPart > targetPart
        }

        return true
    }

    private fun String.versionParts(): List<Int> =
        substringBefore('-')
            .split('.')
            .map { it.toIntOrNull() ?: 0 }
            .let { parts -> (parts + listOf(0, 0, 0)).take(VERSION_PART_COUNT) }

    private companion object {
        const val VERSION_PART_COUNT = 3
    }
}
