package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.airplane.TurnOffAirplaneModeRequester
import net.m3mobile.feature.startup.requester.airplane.TurnOnAirplaneModeRequester

internal class AirplaneModeApiImpl(private val context: Context): AirplaneModeApi {

    override fun turnOnAirplaneMode() {
        TurnOnAirplaneModeRequester(context).request()
    }

    override fun turnOffAirplaneMode() {
        TurnOffAirplaneModeRequester(context).request()
    }
}