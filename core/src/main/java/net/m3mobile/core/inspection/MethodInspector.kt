package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.core.device.currentDeviceModel
import net.m3mobile.core.source.MethodMapSource
import java.lang.reflect.Method
import java.util.ServiceLoader

@InternalM3Api
public abstract class MethodInspector<T: MethodMapSource, V: Any>: Inspector {

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
        assert(method.inspectionKey(), methodName)
    }

    protected abstract fun assert(methodKey: String, methodName: String)
}

internal fun Method.inspectionKey(): String {
    val sourceParameterTypes = parameterTypes
        .toList()
        .let { types ->
            if (types.lastOrNull()?.name == "kotlin.coroutines.Continuation") {
                types.dropLast(1)
            } else {
                types
            }
        }

    return declaringClass.name + "." + name.substringBefore('-') +
        sourceParameterTypes.mapIndexed { index, type ->
            val sourceType =
                if (isVarArgs && index == sourceParameterTypes.lastIndex) type.componentType else type
            sourceType.kotlinSourceName()
        }.joinToString(prefix = "(", postfix = ")")
}

private fun Class<*>.kotlinSourceName(): String = when (this) {
    java.lang.Boolean.TYPE -> "Boolean"
    java.lang.Byte.TYPE -> "Byte"
    java.lang.Character.TYPE -> "Char"
    java.lang.Short.TYPE -> "Short"
    java.lang.Integer.TYPE -> "Int"
    java.lang.Long.TYPE -> "Long"
    java.lang.Float.TYPE -> "Float"
    java.lang.Double.TYPE -> "Double"
    else -> simpleName
}
