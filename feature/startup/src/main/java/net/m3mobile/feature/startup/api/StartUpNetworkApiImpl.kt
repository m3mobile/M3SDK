package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.params.Apn
import net.m3mobile.feature.startup.requester.network.DisableNfcRequester
import net.m3mobile.feature.startup.requester.network.EnableNfcRequester
import net.m3mobile.feature.startup.requester.network.SetApnRequester

internal class StartUpNetworkApiImpl(private val context: Context): StartUpNetworkApi {

    override fun setApn(apn: Apn) {
        SetApnRequester(context, apn).request()
    }

    override fun enableNfc() {
        EnableNfcRequester(context).request()
    }

    override fun disableNfc() {
        DisableNfcRequester(context).request()
    }
}