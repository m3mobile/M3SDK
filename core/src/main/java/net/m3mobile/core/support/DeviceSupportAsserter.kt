package net.m3mobile.core.support

import android.os.Build
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsupportedDeviceModelException
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

@InternalM3Api
object DeviceSupportAsserter {

    private val apiMethodsAvailabilityCache = ConcurrentHashMap<Method, Boolean>()

    /**
     * 현재 디바이스의 모델이 해당 메서드를 지원하는지 런타임에 확인합니다.
     *
     * @param method 지원 여부를 검사할 메서드
     * @throws UnsupportedDeviceModelException 지원되지 않는 모델일 경우
     */
    @JvmSynthetic
    fun assertIsDeviceSupported(method: Method) {
        val methodName = method.name
        val isSupported = apiMethodsAvailabilityCache[method]
        if (isSupported != null) {
            if (!isSupported) {
                val annotation = method.getAnnotation(SupportedModels::class.java)
                throw UnsupportedDeviceModelException(methodName, annotation?.models ?: emptyArray())
            }
            return
        }

        val supportedModelsAnnotation = method.getAnnotation(SupportedModels::class.java)
        if (supportedModelsAnnotation == null) {
            apiMethodsAvailabilityCache[method] = true
            return
        }

        val supportedModelList = supportedModelsAnnotation.models
        val model = Build.MODEL
        val isNowSupported = model in supportedModelList
        apiMethodsAvailabilityCache[method] = isNowSupported

        if (!isNowSupported)
            throw UnsupportedDeviceModelException(methodName, supportedModelList)
    }
}