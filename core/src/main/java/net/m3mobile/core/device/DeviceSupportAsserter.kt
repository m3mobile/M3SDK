package net.m3mobile.core.device

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.UnsupportedModels
import java.util.ServiceLoader

@InternalM3Api
object DeviceSupportAsserter {

    internal val currentDeviceModel by lazy {
        getCurrentDeviceModel()
    }

    /**
     * **key** 전체 메서드 이름
     *
     * **value** 지원하는 기기 집합
     */
    private val deviceSupportMap: Map<String, Set<String>> by lazy {
        val serviceLoader = ServiceLoader.load(DeviceSupportProvider::class.java)
        serviceLoader.fold(mutableMapOf()) { acc, provider ->
            acc.putAll(provider.getSupportMap())
            acc
        }
    }

    /**
     * 현재 디바이스 모델이 해당 메서드를 지원하는지 검사합니다.
     *
     * [SupportedModels]와 [UnsupportedModels] 어노테이션을 기반으로 검사가 이루어지며,
     * 하나의 메서드에 두 어노테이션이 모두 존재할 경우 [UnsupportedModels]는 무시됩니다.
     *
     * @throws UnsupportedDeviceModelException 지원하지 않는 모델일 경우
     */
    @JvmSynthetic
    fun assertIsDeviceSupported(methodKey: String, methodName: String) {
        val supportedModels = deviceSupportMap[methodKey] ?: DeviceModel.setOfEntriesName

        if (currentDeviceModel.name !in supportedModels) {
            throw UnsupportedDeviceModelException(
                methodName,
                supportedModels.toTypedArray()
            )
        }
    }
}