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