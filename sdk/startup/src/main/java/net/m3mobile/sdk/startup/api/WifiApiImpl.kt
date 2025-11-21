package net.m3mobile.sdk.startup.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.launchOnMain
import net.m3mobile.sdk.startup.requester.wifi.AllowAllFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.AllowOnly2_4GHzFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.AllowOnly5GHzFrequencyBandRequester
import net.m3mobile.sdk.startup.requester.wifi.DisableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.DisableOpenNetworkNotiRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableCaptivePortalDetectionRequester
import net.m3mobile.sdk.startup.requester.wifi.EnableOpenNetworkNotiRequester
import net.m3mobile.sdk.startup.requester.wifi.GetWifiMacRequester
import net.m3mobile.sdk.startup.requester.wifi.SetAccessPointRequester
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
        SetRoamingTriggerRequester(context, index).request()
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
}

@ConsistentCopyVisibility
data class AccessPoint private constructor(
    val ssid: String,
    val security: String,
    val password: String?,
    val enableStatic: Boolean?,
    val ipAddress: String?,
    val mask: String?,
    val gateway: String?,
    val dns: String?,
    val macRandom: Boolean?,
    val hiddenSsid: Boolean?
) {

    interface SsidBuilder {
        fun setSsid(ssid: String): SecurityBuilder
    }

    interface SecurityBuilder {
        fun setSecurity(security: String): Builder
    }

    /**
     * Example:
     * ```kotlin
     * AccessPoint.builder()
     *      .setSsid("ssid")
     *      .setSecurity("security")
     *      .build()
     * ```
     */
    class Builder internal constructor(): SsidBuilder, SecurityBuilder {
        private lateinit var ssid: String
        private lateinit var security: String
        private var password: String? = null
        private var enableStatic: Boolean? = null
        private var ipAddress: String? = null
        private var mask: String? = null
        private var gateway: String? = null
        private var dns: String? = null
        private var macRandom: Boolean? = null
        private var hiddenSsid: Boolean? = null

        override fun setSsid(ssid: String) = apply { this.ssid = ssid } as SecurityBuilder
        override fun setSecurity(security: String) = apply { this.security = security }
        fun setPassword(password: String) = apply { this.password = password }
        fun setEnableStatic(enable: Boolean) = apply { this.enableStatic = enable }
        fun setIpAddress(ipAddress: String) = apply { this.ipAddress = ipAddress }
        fun setMask(mask: String) = apply { this.mask = mask }
        fun setGateway(gateway: String) = apply { this.gateway = gateway }
        fun setDns(dns: String) = apply { this.dns = dns }
        fun setMacRandom(macRandom: Boolean) = apply { this.macRandom = macRandom }
        fun setHiddenSsid(hiddenSsid: Boolean) = apply { this.hiddenSsid = hiddenSsid }

        fun build() = AccessPoint(
            ssid = ssid,
            security = security,
            password = password,
            enableStatic = enableStatic,
            ipAddress = ipAddress,
            mask = mask,
            gateway = gateway,
            dns = dns,
            macRandom = macRandom,
            hiddenSsid = hiddenSsid
        )
    }

    companion object {
        fun builder() = Builder()
    }
}