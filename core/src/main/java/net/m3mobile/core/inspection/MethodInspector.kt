package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.core.device.currentDeviceModel
import net.m3mobile.core.source.MethodMapSource
import java.lang.reflect.Method
import java.util.ServiceLoader

@InternalM3Api
abstract class MethodInspector<T: MethodMapSource, V: Any>: Inspector {

    protected abstract val serviceLoader: ServiceLoader<T>
    protected val methodMap: Map<String, V> by lazy {
        serviceLoader.fold(mutableMapOf()) { acc, provider ->
            acc.putAll(provider.get())
            acc
        }
    }

    override fun invoke(method: Method) {
        if (currentDeviceModel == DeviceModel.UNKNOWN)
            return
        
        val methodName = method.name.substringBefore('-')
        val methodKey =
            method.declaringClass.name + "." + methodName + method.parameterTypes.joinToString(
                prefix = "(",
                postfix = ")"
            ) { it.name }

        assert(methodKey, methodName)
    }

    protected abstract fun assert(methodKey: String, methodName: String)
}