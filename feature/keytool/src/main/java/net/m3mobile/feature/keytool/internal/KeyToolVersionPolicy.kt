package net.m3mobile.feature.keytool.internal

import net.m3mobile.core.device.DeviceModel

internal object KeyToolVersionPolicy {

    const val FN_MINIMUM_VERSION = "1.2.6"
    const val NAVIGATION_MINIMUM_VERSION = "1.4.1"
    const val SCAN_WAKE_UP_MINIMUM_VERSION = "1.3.8"

    fun keyFunctionMinimumVersion(model: DeviceModel): String = when (model) {
        DeviceModel.SM24 -> "1.3.8"
        DeviceModel.SM25 -> "1.3.16"
        else -> "1.2.6"
    }

    fun versionSatisfied(currentVersion: String, requiredVersion: String): Boolean {
        val current = versionParts(currentVersion)
        val required = versionParts(requiredVersion)

        return current.zip(required)
            .firstOrNull { (currentPart, requiredPart) -> currentPart != requiredPart }
            ?.let { (currentPart, requiredPart) -> currentPart > requiredPart }
            ?: true
    }

    private fun versionParts(version: String): List<Int> {
        val tokens = version.split('.')
        return List(VERSION_PART_COUNT) { index ->
            tokens.getOrNull(index)
                ?.takeWhile(Char::isDigit)
                ?.toIntOrNull()
                ?: 0
        }
    }

    private const val VERSION_PART_COUNT = 3
}
