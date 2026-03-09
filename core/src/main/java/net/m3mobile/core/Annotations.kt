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
public annotation class InternalM3Api

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
public annotation class SupportedModels(vararg val models: DeviceModel)

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
public annotation class UnsupportedModels(vararg val models: DeviceModel)

/**
 * Specifies a per-model version override for use inside [RequiresStartUp] or [RequiresScanEmul].
 *
 * @property model The device model this override applies to.
 * @property version The minimum app version required for the specified model.
 */
@Retention(AnnotationRetention.BINARY)
@Target()
@InternalM3Api
public annotation class ModelVersion(
    val model: DeviceModel,
    val version: String
)

/**
 * This indicates that the API will only function if the version of the `StartUp` app
 * installed on the device is equal to or later than the specified value.
 *
 * When `STRICT_MODE` is enabled, an [UnsatisfiedVersionException] exception is thrown
 * if the installed `StartUp` app version is earlier than the specified value.
 *
 * When `STRICT_MODE` is disabled, no exception is thrown.
 *
 * `STRICT_MODE` is disabled by default. To enable it, please refer to the SDK documentation.
 *
 * Example:
 * ```
 * @RequiresStartUp("1.1.2")
 * fun someAPI() // This API will only function if the StartUp version installed on the device is 1.1.2 or later.
 *
 * @RequiresStartUp("6.5.35", ModelVersion(DeviceModel.UL30, "6.5.31"))
 * fun anotherAPI() // UL30 requires 6.5.31, all others require 6.5.35.
 * ```
 *
 * @property version The default minimum StartUp version required.
 * @property overrides Per-model version overrides. If the current device matches an override, its version is used instead.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@InternalM3Api
public annotation class RequiresStartUp(
    val version: String,
    vararg val overrides: ModelVersion
)

/**
 * This indicates that the API will only function if the version of the `ScanEmul` app
 * installed on the device is equal to or later than the specified value.
 *
 * When `STRICT_MODE` is enabled, an [UnsatisfiedVersionException] exception is thrown
 * if the installed `ScanEmul` app version is earlier than the specified value.
 *
 * When `STRICT_MODE` is disabled, no exception is thrown.
 *
 * `STRICT_MODE` is disabled by default. To enable it, please refer to the SDK documentation.
 *
 * Example:
 * ```
 * @RequiresScanEmul("1.1.2")
 * fun someAPI() // This API will only function if the ScanEmul version installed on the device is 1.1.2 or later.
 * ```
 *
 * @property version The default minimum ScanEmul version required.
 * @property overrides Per-model version overrides. If the current device matches an override, its version is used instead.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
@InternalM3Api
public annotation class RequiresScanEmul(
    val version: String,
    vararg val overrides: ModelVersion
)