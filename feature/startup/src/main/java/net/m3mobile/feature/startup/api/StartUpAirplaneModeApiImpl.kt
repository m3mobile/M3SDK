package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.airplane.TurnOffAirplaneModeRequester
import net.m3mobile.feature.startup.requester.airplane.TurnOnAirplaneModeRequester

internal class StartUpAirplaneModeApiImpl(private val context: Context): StartUpAirplaneModeApi {

    override fun turnOnAirplaneMode() {
        TurnOnAirplaneModeRequester(context).request()
    }

    override fun turnOffAirplaneMode() {
        TurnOffAirplaneModeRequester(context).request()
    }
}