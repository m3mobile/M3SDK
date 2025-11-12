package net.m3mobile.core

/**
 * Marks declarations that are part of the M3SDK's internal API.
 *
 * This annotation signifies that a class, function, or property is intended for internal use
 * within the M3SDK library only. It is not part of the public API and its usage is not supported.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is for internal use within the M3SDK. Do not call it directly."
)
annotation class InternalM3Api

/**
 * Specifies the M3 Mobile device models that a particular function supports.
 *
 * This annotation is used internally by the M3SDK to check if a function is compatible with the
 * current device model. If the device model is not in the list of supported models,
 * an [UnsupportedDeviceModelException] will be thrown when the function is called.
 *
 * Functions without this annotation are considered to be callable on all models.
 *
 * @property models An array of models that support the annotated function.
 * @see UnsupportedDeviceModelException
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@InternalM3Api
annotation class SupportedModels(vararg val models: DeviceModel)