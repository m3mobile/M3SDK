@file:OptIn(net.m3mobile.core.InternalM3Api::class)

package net.m3mobile.feature.keytool.api

import android.content.Context
import net.m3mobile.core.device.currentDeviceModel
import net.m3mobile.feature.keytool.KeyToolAppGuard
import net.m3mobile.feature.keytool.internal.ExplicitKeySettingBroadcast
import net.m3mobile.feature.keytool.internal.KeySettingRequestFactory
import net.m3mobile.feature.keytool.internal.KeyToolVersionPolicy
import net.m3mobile.feature.keytool.requester.key.DisableFnRequester
import net.m3mobile.feature.keytool.requester.key.EnableFnRequester
import net.m3mobile.feature.keytool.requester.key.LockFnRequester

internal class KeyToolKeyApiImpl(private val context: Context): KeyToolKeyApi {

    private val appGuard = KeyToolAppGuard(context)
    private val broadcast = ExplicitKeySettingBroadcast(context)

    override fun enableFN() {
        assertSl20AppVersion("enableFN", KeyToolVersionPolicy.FN_MINIMUM_VERSION)
        EnableFnRequester(context).request()
    }

    override fun disableFN() {
        assertSl20AppVersion("disableFN", KeyToolVersionPolicy.FN_MINIMUM_VERSION)
        DisableFnRequester(context).request()
    }

    override fun lockFN() {
        assertSl20AppVersion("lockFN", KeyToolVersionPolicy.FN_MINIMUM_VERSION)
        LockFnRequester(context).request()
    }

    override fun setKeyFunction(key: String, function: String) {
        setKeyFunction(
            key = key,
            function = function,
            wakeUpEnabled = null,
            requiredVersion = KeyToolVersionPolicy.keyFunctionMinimumVersion(currentDeviceModel),
        )
    }

    override fun setKeyFunction(key: String, function: String, wakeUpEnabled: Boolean) {
        setKeyFunction(
            key = key,
            function = function,
            wakeUpEnabled = wakeUpEnabled,
            requiredVersion = KeyToolVersionPolicy.SCAN_WAKE_UP_MINIMUM_VERSION,
        )
    }

    override fun enableHomeButton() {
        setNavigationButton("enableHomeButton", HOME, DEFAULT)
    }

    override fun disableHomeButton() {
        setNavigationButton("disableHomeButton", HOME, DISABLE)
    }

    override fun enableRecentButton() {
        setNavigationButton("enableRecentButton", RECENT, DEFAULT)
    }

    override fun disableRecentButton() {
        setNavigationButton("disableRecentButton", RECENT, DISABLE)
    }

    private fun setNavigationButton(methodName: String, key: String, function: String) {
        assertSl20AppVersion(methodName, KeyToolVersionPolicy.NAVIGATION_MINIMUM_VERSION)
        broadcast.send(KeySettingRequestFactory.keyFunctionRequest(key, function))
    }

    private fun setKeyFunction(
        key: String,
        function: String,
        wakeUpEnabled: Boolean?,
        requiredVersion: String,
    ) {
        require(key.isNotBlank()) { "A KeyTool key title is required." }
        require(function.isNotBlank()) { "A KeyTool function title is required." }
        assertSl20AppVersion("setKeyFunction", requiredVersion)
        broadcast.send(
            KeySettingRequestFactory.keyFunctionRequest(key, function, wakeUpEnabled)
        )
    }

    private fun assertSl20AppVersion(methodName: String, requiredVersion: String) {
        appGuard.assertSl20AppVersion(methodName, currentDeviceModel, requiredVersion)
    }

    private companion object {
        const val HOME = "Home"
        const val RECENT = "Recent"
        const val DEFAULT = "Default"
        const val DISABLE = "Disable"
    }
}
