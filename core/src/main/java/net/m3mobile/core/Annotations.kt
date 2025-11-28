package net.m3mobile.core

import net.m3mobile.core.device.DeviceModel

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
 * When `STRICT_MODE` is enabled,
 * if the device model is not in the list of supported models,
 * an [UnsupportedDeviceModelException] will be thrown when the function is called.
 *
 * When `STRICT_MODE` is disabled, no exception is thrown.
 *
 * `STRICT_MODE` is disabled by default. To enable it, please refer to the SDK documentation.
 *
 * Functions without this annotation and [UnsupportedModels] are considered to be callable on all models.
 *
 * @property models An array of models that support the annotated function.
 * @see UnsupportedModels
 * @see UnsupportedDeviceModelException
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@InternalM3Api
annotation class SupportedModels(vararg val models: DeviceModel)

/**
 * Specifies the M3 Mobile device models that a particular function does NOT support.
 *
 * When `STRICT_MODE` is enabled,
 * if the device model is not in the list of supported models,
 * an [UnsupportedDeviceModelException] will be thrown when the function is called.
 *
 * When `STRICT_MODE` is disabled, no exception is thrown.
 *
 * `STRICT_MODE` is disabled by default. To enable it, please refer to the SDK documentation.
 *
 * This provides an alternative to [SupportedModels] for cases where a function works on most models
 * but needs to be explicitly blocked on a few.
 *
 * Functions without this annotation and [SupportedModels] are considered to be callable on all models.
 *
 * @property models An array of models that do NOT support the annotated function.
 * @see SupportedModels
 * @see UnsupportedDeviceModelException
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@InternalM3Api
annotation class UnsupportedModels(vararg val models: DeviceModel)