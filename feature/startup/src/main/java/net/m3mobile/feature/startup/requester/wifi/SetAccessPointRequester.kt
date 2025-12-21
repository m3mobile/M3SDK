package net.m3mobile.feature.startup.requester.wifi

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue
import net.m3mobile.feature.startup.params.AccessPoint

internal class SetAccessPointRequester(
    override val context: Context,
    accessPoint: AccessPoint
) : BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.ACCESS_POINT
    override val extras = buildExtraBundle(accessPoint)

    private fun buildExtraBundle(ap: AccessPoint) = bundleOf(
        ExtraKey.SET_AP_SSID to ap.ssid,
        ExtraKey.SET_AP_SECURITY to ap.security
    ).apply {
        if (ap.password != null)
            putString(ExtraKey.SET_AP_PASSWORD, ap.password)
        if (ap.enableStatic != null)
            putBoolean(ExtraKey.ENABLE_AP_STATIC, ap.enableStatic)
        if (ap.ipAddress != null)
            putString(ExtraKey.SET_AP_IP, ap.ipAddress)
        if (ap.mask != null)
            putString(ExtraKey.SET_AP_MASK, ap.mask)
        if (ap.gateway != null)
            putString(ExtraKey.SET_AP_GATEWAY, ap.gateway)
        if (ap.dns != null)
            putString(ExtraKey.SET_AP_DNS, ap.dns)
        if (ap.macRandom != null)
            putBoolean(ExtraKey.SET_AP_MAC_RANDOM, ap.macRandom)
        if (ap.hiddenSsid != null)
            putBoolean(ExtraKey.SET_AP_HIDDEN_SSID, ap.hiddenSsid)
    }
}