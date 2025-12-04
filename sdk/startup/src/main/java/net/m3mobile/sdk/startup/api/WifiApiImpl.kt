package net.m3mobile.sdk.startup.api

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.utils.getGlobalInt
import net.m3mobile.core.utils.getGlobalString
import net.m3mobile.core.utils.launchOnMain
import net.m3mobile.sdk.startup.params.AccessPoint
import net.m3mobile.sdk.startup.requester.wifi.AllowAllFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.AllowOnly2_4GHzFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.AllowOnly5GHzFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.ClearSavedWifiNetworksRequester
import net.m3mobile.sdk.startup.requester.wifi.DisableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.DisableOpenNetworkNotiRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableOpenNetworkNotiRequester
import net.m3mobile.sdk.startup.requester.wifi.GetWifiMacRequester
import net.m3mobile.sdk.startup.requester.wifi.RemoveWifiNetworkRequester
import net.m3mobile.sdk.startup.requester.wifi.SetAccessPointRequester
import net.m3mobile.sdk.startup.requester.wifi.SetRoamingDeltaRequester
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
        return GetWifiMacRequester(context).fetch()
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
        EnableCaptivePortalDetectionRequester(context).request()
    }

    override fun disableCaptivePortalDetection() {
        DisableCaptivePortalDetectionRequester(context).request()
    }

    override fun allowAllWifiFrequencyBand() {
        AllowAllFrequencyBandRequester(context).request()
    }

    override fun allowOnly2_4GHzWifiFrequencyBand() {
        AllowOnly2_4GHzFrequencyBandRequester(context).request()
    }

    override fun allowOnly5GHzWifiFrequencyBand() {
        AllowOnly5GHzFrequencyBandRequester(context).request()
    }

    override fun setWifiCountry(countryCode: String) {
        SetWifiCountryRequester(context, countryCode).request()
    }

    override fun enableOpenNetworkNotification() {
        EnableOpenNetworkNotiRequester(context).request()
    }

    override fun disableOpenNetworkNotification() {
        DisableOpenNetworkNotiRequester(context).request()
    }

    override fun setRoamingTrigger(index: Int) {
        SetRoamingTriggerRequester(context, index).request()
    }

    override fun setRoamingDelta(index: Int) {
        SetRoamingDeltaRequester(context, index).request()
    }

    override fun setWifiSleepPolicyNever() {
        SetWifiSleepPolicyNeverRequester(context).request()
    }

    override fun setWifiSleepPolicyPluggedOnly() {
        SetWifiSleepPolicyPluggedOnlyRequester(context).request()
    }

    override fun setWifiSleepPolicyAlways() {
        SetWifiSleepPolicyAlwaysRequester(context).request()
    }

    override fun setWifiStabilityNormal() {
        SetWifiStabilityNormalRequester(context).request()
    }

    override fun setWifiStabilityHigh() {
        SetWifiStabilityHighRequester(context).request()
    }

    override fun setWifiChannel(vararg channels: Int) {
        SetWifiChannelRequester(context, channels.map { it.toString() }.toTypedArray()).request()
    }

    override fun setAccessPoint(accessPoint: AccessPoint) {
        SetAccessPointRequester(context, accessPoint).request()
    }

    override fun clearSavedWifiNetworks() {
        ClearSavedWifiNetworksRequester(context).request()
    }

    override fun removeWifiNetwork(ssid: String) {
        RemoveWifiNetworkRequester(context, ssid).request()
    }

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

    override suspend fun getWifiCountryCode(): String {
        val code = context.getGlobalString("wifi_country_code")
        return code
    }
}