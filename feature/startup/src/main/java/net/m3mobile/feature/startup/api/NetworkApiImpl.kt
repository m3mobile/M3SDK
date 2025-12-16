package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.params.Apn
import net.m3mobile.feature.startup.requester.network.SetApnRequester

internal class NetworkApiImpl(private val context: Context): NetworkApi {

    override fun setApn(apn: Apn) {
        SetApnRequester(context, apn).request()
    }
}