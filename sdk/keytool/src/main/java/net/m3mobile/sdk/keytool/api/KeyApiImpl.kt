package net.m3mobile.sdk.keytool.api

import android.content.Context
import net.m3mobile.sdk.keytool.requester.key.DisableFnRequester
import net.m3mobile.sdk.keytool.requester.key.EnableFnRequester
import net.m3mobile.sdk.keytool.requester.key.LockFnRequester

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
}