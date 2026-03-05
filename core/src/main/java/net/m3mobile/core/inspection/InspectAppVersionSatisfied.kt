package net.m3mobile.core.inspection

import android.content.pm.PackageManager
import android.os.Build
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.UnsatisfiedVersionException
import net.m3mobile.core.appContext
import net.m3mobile.core.device.currentDeviceModel
import net.m3mobile.core.source.MethodMapSource
import java.util.ServiceLoader

/**
 * 현재 디바이스에 지정된 앱(패키지)가 설치되어 있는지, 그리고 설치된 버전이 요구 사항을 충족하는지 검사합니다.
 *
 * 조건이 충족되지 않을 경우 호출부로 [UnsatisfiedVersionException]를 던집니다.
 *
 * @param T 기본 버전 맵 소스 타입
 * @param U 모델별 버전 override 맵 소스 타입
 */
@InternalM3Api
public abstract class InspectAppVersionSatisfied<T: MethodMapSource, U: MethodMapSource>
    : MethodInspector<T, String>() {

    protected abstract val appName: String
    protected abstract val appPackage: String
    protected abstract val modelVersionServiceLoader: ServiceLoader<U>

    @Suppress("UNCHECKED_CAST")
    private val modelVersionMap: Map<String, Map<String, String>> by lazy {
        modelVersionServiceLoader.fold(mutableMapOf<String, Map<String, String>>()) { acc, provider ->
            acc.putAll(provider.get<Map<String, String>>() as Map<String, Map<String, String>>)
            acc
        }
    }

    override fun assert(methodKey: String, methodName: String) {
        val defaultVersion = methodMap[methodKey] ?: return

        val overrides = modelVersionMap[methodKey]
        val effectiveVersion = overrides?.get(currentDeviceModel.name) ?: defaultVersion

        val currentAppVersion = getAppVersionName() ?: throw UnsatisfiedVersionException(
            methodName,
            appName,
            effectiveVersion
        )

        if (!currentAppVersion.isVersionAbove(effectiveVersion))
            throw UnsatisfiedVersionException(
                methodName,
                appName,
                effectiveVersion,
                effectiveVersion
            )
    }

    private fun getAppVersionName(): String? {
        return try {
            val packageManager = appContext.packageManager

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