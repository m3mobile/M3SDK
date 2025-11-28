package net.m3mobile.core.proxy

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.UnsatisfiedVersionException
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.device.DeviceSupportAsserter
import java.lang.reflect.Method

/**
 * Method를 실행한 디바이스 모델이 해당 Method를 지원하는지 검사합니다.
 *
 * 지원하지 않는 디바이스 모델일 경우 호출부로 [UnsupportedDeviceModelException]를 던집니다.
 */
@InternalM3Api
class InspectDeviceSupport: (Method) -> Unit {
    override fun invoke(method: Method) {
        val methodName = method.name.substringBefore('-')
        val methodKey =
            method.declaringClass.name + "." + methodName + method.parameterTypes.joinToString(
                prefix = "(",
                postfix = ")"
            ) { it.name }

        DeviceSupportAsserter.assertIsDeviceSupported(methodKey, methodName)
    }
}

/**
 * 현재 디바이스에 지정된 앱(패키지)가 설치되어 있는지, 그리고 설치된 버전이 요구 사항을 충족하는지 검사합니다.
 *
 * 조건이 충족되지 않을 경우 호출부로 [UnsatisfiedVersionException]를 던집니다.
 */
@InternalM3Api
class InspectAppVersionSatisfied : (Method) -> Unit {
    override fun invoke(method: Method) {
        TODO("앱 버전 검사 로직 구현")
    }
}