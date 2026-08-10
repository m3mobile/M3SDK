package net.m3mobile.core.inspection

import net.m3mobile.core.RequestCallback
import org.junit.Assert.assertEquals
import org.junit.Test

class MethodInspectorTest {

    @Test
    fun `inspection key uses Kotlin primitive type names`() {
        val method = SampleApi::class.java.getDeclaredMethod(
            "setWifiEnabled",
            Boolean::class.javaPrimitiveType,
        )

        assertEquals(
            "${SampleApi::class.java.name}.setWifiEnabled(Boolean)",
            method.inspectionKey(),
        )
    }

    @Test
    fun `inspection key represents vararg component type`() {
        val method = SampleApi::class.java.getDeclaredMethod(
            "setWifiChannel",
            IntArray::class.java,
        )

        assertEquals(
            "${SampleApi::class.java.name}.setWifiChannel(Int)",
            method.inspectionKey(),
        )
    }

    @Test
    fun `inspection key removes suspend continuation parameter`() {
        val method = SampleApi::class.java.getDeclaredMethod(
            "getWifiState",
            String::class.java,
            kotlin.coroutines.Continuation::class.java,
        )

        assertEquals(
            "${SampleApi::class.java.name}.getWifiState(String)",
            method.inspectionKey(),
        )
    }

    @Test
    fun `inspection key keeps callback and object overload parameter names`() {
        val callbackMethod = SampleApi::class.java.getDeclaredMethod(
            "setWifiState",
            RequestCallback::class.java,
        )
        val objectMethod = SampleApi::class.java.getDeclaredMethod("setWifiState", Any::class.java)

        assertEquals(
            "${SampleApi::class.java.name}.setWifiState(RequestCallback)",
            callbackMethod.inspectionKey(),
        )
        assertEquals(
            "${SampleApi::class.java.name}.setWifiState(Object)",
            objectMethod.inspectionKey(),
        )
    }

    private interface SampleApi {
        fun setWifiEnabled(enabled: Boolean)
        fun setWifiChannel(vararg channels: Int)
        suspend fun getWifiState(requestId: String): String
        fun setWifiState(callback: RequestCallback<String>)
        fun setWifiState(value: Any)
    }
}
