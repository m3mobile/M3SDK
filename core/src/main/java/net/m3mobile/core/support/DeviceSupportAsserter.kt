package net.m3mobile.core.support

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.getDeviceModel
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

@InternalM3Api
object DeviceSupportAsserter {

    private val apiMethodsAvailabilityCache = ConcurrentHashMap<Method, Boolean>()
    private val deviceModel by lazy {
        getDeviceModel()
    }

    /**
     * 현재 디바이스 모델이 해당 메서드를 지원하는지 검사합니다.
     *
     * @param method 지원 여부를 검사할 메서드
     * @throws UnsupportedDeviceModelException 지원하지 않는 모델일 경우
     */
    @JvmSynthetic
    fun assertIsDeviceSupported(method: Method) {
        val methodName = method.name
        val isSupported = apiMethodsAvailabilityCache[method]
        if (isSupported != null) {
            if (!isSupported) {
                val annotation = method.getAnnotation(SupportedModels::class.java)
                throw UnsupportedDeviceModelException(
                    methodName,
                    annotation?.models?.map { it.name }?.toTypedArray() ?: emptyArray()
                )
            }
            return
        }

        val supportedModelsAnnotation = method.getAnnotation(SupportedModels::class.java)
        if (supportedModelsAnnotation == null) {
            apiMethodsAvailabilityCache[method] = true
            return
        }

        val supportedModelList = supportedModelsAnnotation.models
        val isNowSupported = deviceModel in supportedModelList
        apiMethodsAvailabilityCache[method] = isNowSupported

        if (!isNowSupported)
            throw UnsupportedDeviceModelException(methodName, supportedModelList.map { it.name }.toTypedArray())
    }
}