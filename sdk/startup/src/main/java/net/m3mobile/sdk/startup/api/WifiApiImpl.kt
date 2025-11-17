package net.m3mobile.sdk.startup.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.launchOnMain
import net.m3mobile.sdk.startup.requester.wifi.DisableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.GetWifiMacRequester
import net.m3mobile.sdk.startup.requester.wifi.SetFrequencyBandRequester

internal class WifiApiImpl(private val context: Context): WifiApi {

    override suspend fun getWifiMac(): String {
        return GetWifiMacRequester(context).requestResult()
    }

    override fun getWifiMac(callback: RequestCallback<String>): Job {
        return launchOnMain {
            try {
                val wifiMac = getWifiMac()
                callback.onComplete(wifiMac, null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }

    override fun enableCaptivePortalDetection() {
        EnableCaptivePortalDetectionRequester(context).runBroadcast()
    }

    override fun disableCaptivePortalDetection() {
        DisableCaptivePortalDetectionRequester(context).runBroadcast()
    }

    override fun setWifiFrequencyBand(band: WifiFrequencyBandMode) {
        SetFrequencyBandRequester(context, band).runBroadcast()
    }
}

enum class WifiFrequencyBandMode {
    /**
     * Supports all bands.
     */
    ALL,

    /** Only 2.4GHz band */
    _2GHz,

    /** Only 5GHz band */
    _5GHz,
}