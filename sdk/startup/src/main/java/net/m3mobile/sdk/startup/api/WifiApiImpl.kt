package net.m3mobile.sdk.startup.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.launchOnMain
import net.m3mobile.sdk.startup.requester.wifi.DisableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.DisableOpenNetworkNotiRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableOpenNetworkNotiRequester
import net.m3mobile.sdk.startup.requester.wifi.GetWifiMacRequester
import net.m3mobile.sdk.startup.requester.wifi.SetFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.SetRoamingTriggerRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiChannelRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiCountryRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiSleepPolicyAlwaysRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiSleepPolicyNeverRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiSleepPolicyPluggedOnlyRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiStabilityHighRequester
import net.m3mobile.sdk.startup.requester.wifi.SetWifiStabilityNormalRequester

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

    override fun setWifiCountry(countryCode: String) {
        SetWifiCountryRequester(context, countryCode).runBroadcast()
    }

    override fun enableOpenNetworkNotification() {
        EnableOpenNetworkNotiRequester(context).runBroadcast()
    }

    override fun disableOpenNetworkNotification() {
        DisableOpenNetworkNotiRequester(context).runBroadcast()
    }

    override fun setRoamingTrigger(index: Int) {
        SetRoamingTriggerRequester(context, index).runBroadcast()
    }

    override fun setRoamingDelta(index: Int) {
        SetRoamingTriggerRequester(context, index).runBroadcast()
    }

    override fun setWifiSleepPolicyNever() {
        SetWifiSleepPolicyNeverRequester(context).runBroadcast()
    }

    override fun setWifiSleepPolicyPluggedOnly() {
        SetWifiSleepPolicyPluggedOnlyRequester(context).runBroadcast()
    }

    override fun setWifiSleepPolicyAlways() {
        SetWifiSleepPolicyAlwaysRequester(context).runBroadcast()
    }

    override fun setWifiStabilityNormal() {
        SetWifiStabilityNormalRequester(context).runBroadcast()
    }

    override fun setWifiStabilityHigh() {
        SetWifiStabilityHighRequester(context).runBroadcast()
    }

    override fun setWifiChannel(vararg channels: Int) {
        SetWifiChannelRequester(context, channels.map { it.toString() }.toTypedArray())
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