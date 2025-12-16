package net.m3mobile.feature.keytool.api

import android.content.Context
import net.m3mobile.feature.keytool.requester.key.DisableFnRequester
import net.m3mobile.feature.keytool.requester.key.EnableFnRequester
import net.m3mobile.feature.keytool.requester.key.LockFnRequester
import net.m3mobile.feature.keytool.requester.key.SetKeyFunctionRequester

internal class KeyApiImpl(private val context: Context): KeyApi {

    override fun enableFN() {
        EnableFnRequester(context).request()
    }

    override fun disableFN() {
        DisableFnRequester(context).request()
    }

    override fun lockFN() {
        LockFnRequester(context).request()
    }

    override fun setKeyFunction(key: String, function: String) {
        SetKeyFunctionRequester(context, key, function).request()
    }
}