package net.m3mobile.core.device

import net.m3mobile.core.InternalM3Api
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

@InternalM3Api
object DeviceSupportProxy {

    /**
     * 주어진 인터페이스 `T`에 대한 동적 프록시를 생성합니다.
     *
     * 이 함수는 인터페이스의 모든 메서드 호출을 가로챕니다. 실제 구현체의 메서드를 호출하기 전에,
     * [DeviceSupportAsserter.assertIsDeviceSupported]를 사용하여 현재 기기가 해당 기능을 지원하는지 먼저 확인합니다.
     * 지원되지 않는 기기일 경우 예외가 발생합니다.
     *
     * @param T 프록시를 생성할 인터페이스 타입
     * @param implementation `T`의 실제 구현 객체
     * @return `T` 타입의 프록시 객체
     */
    @JvmSynthetic
    inline fun <reified T: Any> create(implementation: T): T {
        val interfaceClass = T::class.java
        val handler = InvocationHandler { _, method, args ->
            DeviceSupportAsserter.assertIsDeviceSupported(method)

            method(implementation, *(args ?: emptyArray()))
        }

        return Proxy.newProxyInstance(
            interfaceClass.classLoader,
            arrayOf(interfaceClass),
            handler
        ) as T
    }
}