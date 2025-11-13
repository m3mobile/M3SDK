package net.m3mobile.core.device

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.UnsupportedModels
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

@InternalM3Api
object DeviceSupportAsserter {

    /**
     * [Method]별 지원 정보([MethodSupportInfo])를 캐시하여,
     * 어노테이션 파싱 및 지원 여부 계산에 드는 반복적인 오버헤드를 줄입니다.
     */
    private val supportInfoCache = ConcurrentHashMap<Method, MethodSupportInfo>()
    private val currentDeviceModel by lazy {
        getCurrentDeviceModel()
    }

    /**
     * 현재 디바이스 모델이 해당 메서드를 지원하는지 검사합니다.
     *
     * [SupportedModels]와 [UnsupportedModels] 어노테이션을 기반으로 검사가 이루어지며,
     * 하나의 메서드에 두 어노테이션이 모두 존재할 경우 [UnsupportedModels]는 무시됩니다.
     *
     * @param method 지원 여부를 검사할 메서드
     * @throws UnsupportedDeviceModelException 지원하지 않는 모델일 경우
     */
    @JvmSynthetic
    fun assertIsDeviceSupported(method: Method) {
        val supportInfo = supportInfoCache.getOrPut(method) { MethodSupportInfo(method) }

        if (!supportInfo.isSupported(currentDeviceModel)) {
            throw UnsupportedDeviceModelException(
                method.name,
                supportInfo.supportedModelNames
            )
        }
    }
}

/**
 * 특정 메서드([Method])에 대한 지원 모델 정보를 관리하는 클래스.
 *
 * [SupportedModels]와 [UnsupportedModels] 어노테이션을 파싱하여
 * 해당 메서드가 어떤 [DeviceModel]에서 지원되는지에 대한 정보를 제공합니다.
 *
 * @property method 대상 메서드
 */
@InternalM3Api
private class MethodSupportInfo(method: Method) {

    private val supportedModels: Set<DeviceModel> by lazy {
        val supportedModelsAnnotation = method.getAnnotation(SupportedModels::class.java)
        val unsupportedModelsAnnotation = method.getAnnotation(UnsupportedModels::class.java)

        when {
            supportedModelsAnnotation != null -> supportedModelsAnnotation.models.toSet()
            unsupportedModelsAnnotation != null -> DeviceModel.entries.toSet() - unsupportedModelsAnnotation.models.toSet()
            else -> DeviceModel.entries.toSet()
        }
    }

    val supportedModelNames: Array<String> by lazy {
        supportedModels.map { it.name }.toTypedArray()
    }

    fun isSupported(deviceModel: DeviceModel): Boolean {
        return deviceModel in supportedModels
    }
}