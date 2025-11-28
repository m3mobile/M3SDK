package net.m3mobile.core

import net.m3mobile.core.device.DeviceSupportAsserter

/**
 * Exception thrown when a feature is attempted to be used on an unsupported device model.
 *
 * This exception indicates that the feature is specific to certain
 * M3 Mobile device models and is not available on the current device.
 */
class UnsupportedDeviceModelException(message: String) : RuntimeException(message) {
    constructor(methodName: String, supportedModels: Array<out String>) : this(
        "\"$methodName\" is not available on the current device (${DeviceSupportAsserter.currentDeviceModel.name}). " +
        "Supported models are: ${supportedModels.joinToString(", ")}."
    )
}

/**
 * Exception thrown when a feature requires a specific version of an application that is not met.
 *
 * This exception indicates that the current environment does not satisfy the
 * minimum version requirements needed to execute the requested method or feature.
 */
class UnsatisfiedVersionException(message: String) : RuntimeException(message) {
    constructor(methodName: String, appName: String, appVersion: String, requiredVersion: String) : this(
        "\"$methodName\" is not available on the current $appName version '${appVersion}'. " +
                "Required $appName version is '$requiredVersion'."
    )
}