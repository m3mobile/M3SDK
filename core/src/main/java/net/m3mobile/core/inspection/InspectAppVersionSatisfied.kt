package net.m3mobile.core.inspection

import android.content.pm.PackageManager
import android.os.Build
import net.m3mobile.core.Configurations
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsatisfiedVersionException
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.UnsupportedModels
import net.m3mobile.core.source.MethodMapSource

/**
 * 현재 디바이스에 지정된 앱(패키지)가 설치되어 있는지, 그리고 설치된 버전이 요구 사항을 충족하는지 검사합니다.
 *
 * 조건이 충족되지 않을 경우 호출부로 [UnsatisfiedVersionException]를 던집니다.
 */
@InternalM3Api
abstract class InspectAppVersionSatisfied<T: MethodMapSource> : MethodInspector<T, String>() {

    abstract val appName: String
    abstract val appPackage: String

    override fun assert(methodKey: String, methodName: String) {
        val minStartUpVersion = methodMap[methodKey] ?: return
        val currentStartUpVersion = getAppVersionName() ?: throw UnsatisfiedVersionException(
            methodName,
            appName,
            minStartUpVersion
        )

        if (!currentStartUpVersion.isVersionAbove(minStartUpVersion))
            throw UnsatisfiedVersionException(
                methodName,
                appName,
                minStartUpVersion,
                minStartUpVersion
            )
    }

    private fun getAppVersionName(): String? {
        return try {
            val packageManager = Configurations.appContext.packageManager

            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    appPackage,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getPackageInfo(appPackage, 0)
            }

            packageInfo.versionName
        } catch (_: PackageManager.NameNotFoundException) {
            null
        }
    }

    private fun String.isVersionAbove(required: String): Boolean {
        val current = this.toVersionParts()
        val target = required.toVersionParts()

        val (major, minor, patch) = current
        val (rMajor, rMinor, rPatch) = target

        if (major != rMajor)
            return major > rMajor

        if (minor != rMinor)
            return minor > rMinor

        return patch >= rPatch
    }

    private fun String.toVersionParts(): List<Int> {
        return this.substringBefore('-')
            .split('.')
            .map { it.toIntOrNull() ?: 0 }
    }
}