package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.requester.airplane.TurnOffAirplaneModeRequester
import net.m3mobile.sdk.startup.requester.airplane.TurnOnAirplaneModeRequester

class AirplaneModeApiImpl(private val context: Context): AirplaneModeApi {

    override fun turnOnAirplaneMode() {
        TurnOnAirplaneModeRequester(context).runBroadcast()
    }

    override fun turnOffAirplaneMode() {
        TurnOffAirplaneModeRequester(context).runBroadcast()
    }
}