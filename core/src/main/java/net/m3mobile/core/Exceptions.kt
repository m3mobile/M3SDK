package net.m3mobile.core

import net.m3mobile.core.device.currentDeviceModel

/**
 * Exception thrown when a feature is attempted to be used on an unsupported device model.
 *
 * This exception indicates that the feature is specific to certain
 * M3 Mobile device models and is not available on the current device.
 */
public class UnsupportedDeviceModelException(message: String) : RuntimeException(message) {
    internal constructor(methodName: String, supportedModels: Array<out String>) : this(
        "\"$methodName\" is not available on the current device (${currentDeviceModel.name}). " +
        "Supported models are: ${supportedModels.joinToString(", ")}."
    )
}

/**
 * Exception thrown when a feature requires a specific version of an application that is not met.
 *
 * This exception indicates that the current environment does not satisfy the
 * minimum version requirements needed to execute the requested method or feature.
 */
public class UnsatisfiedVersionException(message: String) : RuntimeException(message) {
    internal constructor(methodName: String, appName: String, appVersion: String, requiredVersion: String) : this(
        "\"$methodName\" is not available on the current $appName version '${appVersion}'. " +
                "Required $appName version is '$requiredVersion'."
    )
    internal constructor(methodName: String, appName: String, requiredVersion: String) : this(
        "\"$methodName\" is not available because $appName is not installed. " +
                "Required $appName version is '$requiredVersion'."
    )
}