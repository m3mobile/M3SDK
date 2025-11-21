package net.m3mobile.sdk.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.api.AccessPoint
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class SetAccessPointRequester(
    override val context: Context,
    accessPoint: AccessPoint
) : BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.ACCESS_POINT
    override val extras = buildExtraBundle(accessPoint)

    private fun buildExtraBundle(ap: AccessPoint) = bundleOf(
        "ssid" to ap.ssid,
        "security" to ap.security
    ).apply {
        if (ap.password != null)
            putString("password", ap.password)
        if (ap.enableStatic != null)
            putBoolean("static_enable", ap.enableStatic)
        if (ap.ipAddress != null)
            putString("ip_address", ap.ipAddress)
        if (ap.mask != null)
            putString("mask", ap.mask)
        if (ap.gateway != null)
            putString("gateway", ap.gateway)
        if (ap.dns != null)
            putString("dns", ap.dns)
        if (ap.macRandom != null)
            putBoolean("mac_random", ap.macRandom)
        if (ap.hiddenSsid != null)
            putBoolean("hidden_ssid", ap.hiddenSsid)
    }
}