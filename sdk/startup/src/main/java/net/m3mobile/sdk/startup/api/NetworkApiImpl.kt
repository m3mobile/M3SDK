package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.params.Apn
import net.m3mobile.sdk.startup.requester.network.SetApnRequester

internal class NetworkApiImpl(private val context: Context): NetworkApi {

    override fun setApn(apn: Apn) {
        SetApnRequester(context, apn).request()
    }
}