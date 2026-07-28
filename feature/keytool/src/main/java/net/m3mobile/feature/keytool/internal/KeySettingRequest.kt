package net.m3mobile.feature.keytool.internal

import net.m3mobile.core.device.DeviceModel
import net.m3mobile.feature.keytool.constants.ExtraKey
import net.m3mobile.feature.keytool.constants.KeyToolContract
import net.m3mobile.feature.keytool.constants.RequestAction

internal class KeySettingRequest internal constructor(
    val packageName: String,
    val action: String,
    val ordered: Boolean,
    private val extras: Map<String, Any>,
) {

    fun extra(key: String): Any? = extras[key]

    fun extras(): Map<String, Any> = extras.toMap()

    override fun toString(): String =
        "KeySettingRequest(packageName=$packageName, action=$action, extras=${extras.keys})"
}

internal object KeySettingRequestFactory {

    fun keyFunctionRequest(
        keyName: String,
        function: String,
        wakeUpEnabled: Boolean? = null,
    ): KeySettingRequest {
        require(keyName.isNotBlank()) { "A KeyTool key title is required." }
        require(function.isNotBlank()) { "A KeyTool function title is required." }
        return unifiedRequest(keyName, function, wakeUpEnabled)
    }

    fun scanWakeUpRequest(
        model: DeviceModel,
        keyName: String,
        enabled: Boolean,
    ): KeySettingRequest {
        require(keyName == KeyToolContract.LEFT_SCAN || keyName == KeyToolContract.RIGHT_SCAN) {
            "Unsupported scan key: $keyName"
        }

        return if (model == DeviceModel.SM24) {
            unifiedRequest(keyName, function = null, wakeUpEnabled = enabled)
        } else {
            legacyWakeUpRequest(keyName, enabled)
        }
    }

    private fun unifiedRequest(
        keyName: String,
        function: String?,
        wakeUpEnabled: Boolean?,
    ): KeySettingRequest {
        require(function != null || wakeUpEnabled != null) {
            "At least one KeyTool setting is required."
        }
        val extras = mutableMapOf<String, Any>(ExtraKey.KEY_TITLE to keyName)
        function?.let { extras[ExtraKey.KEY_FUNCTION] = it }
        wakeUpEnabled?.let { extras[ExtraKey.KEY_WAKEUP] = it }

        return KeySettingRequest(
            packageName = KeyToolContract.SL20_PACKAGE,
            action = RequestAction.ACTION_SET_KEY,
            ordered = true,
            extras = extras,
        )
    }

    private fun legacyWakeUpRequest(keyName: String, enabled: Boolean): KeySettingRequest =
        KeySettingRequest(
            packageName = KeyToolContract.LEGACY_WAKE_UP_PACKAGE,
            action = when (keyName) {
                KeyToolContract.LEFT_SCAN -> RequestAction.LEFT_SCAN_WAKEUP
                else -> RequestAction.RIGHT_SCAN_WAKEUP
            },
            ordered = false,
            extras = mapOf(ExtraKey.ENABLE_WAKEUP to enabled),
        )
}
