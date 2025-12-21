package net.m3mobile.sdk.api

import android.content.Context
import net.m3mobile.core.utils.getGlobalInt
import net.m3mobile.core.utils.getGlobalString

public class WifiApiImpl(private val context: Context): WifiApi {

    override fun getRoamingThreshold(): Int {
        val threshold = context.getGlobalInt("wifi_roam_trigger", -1)
        return threshold
    }

    override fun getRoamingDelta(): Int {
        val delta = context.getGlobalInt("wifi_roam_delta", -1)
        return delta
    }

    override fun getWifiFrequencyBand(): Int {
        val band = context.getGlobalInt("wifi_frequency_band", -1)
        return band
    }

    override fun getWifiCountryCode(): String {
        val code = context.getGlobalString("wifi_country_code")
        return code
    }
}