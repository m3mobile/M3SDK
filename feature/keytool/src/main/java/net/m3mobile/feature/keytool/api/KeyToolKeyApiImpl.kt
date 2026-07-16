package net.m3mobile.feature.keytool.api

import android.content.Context
import net.m3mobile.feature.keytool.KeyToolAppGuard
import net.m3mobile.feature.keytool.requester.key.DisableFnRequester
import net.m3mobile.feature.keytool.requester.key.EnableFnRequester
import net.m3mobile.feature.keytool.requester.key.LockFnRequester
import net.m3mobile.feature.keytool.requester.key.SetKeyFunctionRequester

internal class KeyToolKeyApiImpl(private val context: Context): KeyToolKeyApi {

    private val appGuard = KeyToolAppGuard(context)

    override fun enableFN() {
        appGuard.assertSl20AppAvailable("enableFN")
        EnableFnRequester(context).request()
    }

    override fun disableFN() {
        appGuard.assertSl20AppAvailable("disableFN")
        DisableFnRequester(context).request()
    }

    override fun lockFN() {
        appGuard.assertSl20AppAvailable("lockFN")
        LockFnRequester(context).request()
    }

    override fun setKeyFunction(key: String, function: String) {
        require(key.isNotBlank()) { "A KeyTool key title is required." }
        require(function.isNotBlank()) { "A KeyTool function title is required." }
        appGuard.assertSl20AppAvailable("setKeyFunction")
        SetKeyFunctionRequester(context, key, function).request()
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
        appGuard.assertSl20AppAvailable(methodName)
        SetKeyFunctionRequester(context, key, function).request()
    }

    private companion object {
        const val HOME = "Home"
        const val RECENT = "Recent"
        const val DEFAULT = "Default"
        const val DISABLE = "Disable"
    }
}
