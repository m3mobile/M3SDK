package net.m3mobile.core.proxy

import net.m3mobile.core.IS_STRICT_MODE
import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.inspection.InspectDeviceSupport
import net.m3mobile.core.inspection.InspectStartUpVersionSatisfied
import net.m3mobile.core.inspection.Inspector
import net.m3mobile.core.proxy.ApiProxyFactory.inspections
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

@InternalM3Api
object ApiProxyFactory {

    val inspections by lazy {
        buildList<Inspector> {
            if (IS_STRICT_MODE) {
                add(InspectDeviceSupport())
                add(InspectStartUpVersionSatisfied())
            }
        }
    }

    /**
     * 주어진 인터페이스 `T`에 대한 동적 프록시를 생성합니다.
     *
     * 모든 API 메서드 호출 직전, [inspections]에 나열된 검사를 수행합니다.
     *
     * @param T 프록시를 생성할 인터페이스 타입
     * @param implementation `T`의 실제 구현 객체
     * @return `T` 타입의 프록시 객체
     */
    @JvmSynthetic
    inline fun <reified T: Any> create(implementation: T): T {
        val interfaceClass = T::class.java
        val handler = InvocationHandler { _, method, args ->
            inspections.forEach { inspect -> inspect(method) }

            method.invoke(implementation, *(args ?: emptyArray()))
        }

        return Proxy.newProxyInstance(
            interfaceClass.classLoader,
            arrayOf(interfaceClass),
            handler
        ) as T
    }
}