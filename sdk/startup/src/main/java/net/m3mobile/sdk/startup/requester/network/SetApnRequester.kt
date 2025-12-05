package net.m3mobile.sdk.startup.requester.network

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.FinishRequiredBroadcastRequester
import net.m3mobile.sdk.startup.constants.ExtraKey
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue
import net.m3mobile.sdk.startup.params.Apn

internal class SetApnRequester(
    override val context: Context,
    apn: Apn
) : FinishRequiredBroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.APN
    override val extras = buildExtraBundle(apn)

    private fun buildExtraBundle(apn: Apn) = bundleOf(
        ExtraKey.SET_APN_NAME to apn.name,
        ExtraKey.SET_APN_URL to apn.url,
        ExtraKey.SET_APN_MCC to apn.mcc,
        ExtraKey.SET_APN_MNC to apn.mnc,
        ExtraKey.SET_APN_TYPE to apn.type
    ).apply {
        if (apn.proxy != null)
            putString(ExtraKey.SET_APN_PROXY, apn.proxy)
        if (apn.port != null)
            putString(ExtraKey.SET_APN_PORT, apn.port)
        if (apn.user != null)
            putString(ExtraKey.SET_APN_USER, apn.user)
        if (apn.password != null)
            putString(ExtraKey.SET_APN_SERVER, apn.server)
        if (apn.mmsc != null)
            putString(ExtraKey.SET_APN_MMSC, apn.mmsc)
        if (apn.mmsProxy != null)
            putString(ExtraKey.SET_APN_MMS_PROXY, apn.mmsProxy)
        if (apn.mmsPort != null)
            putString(ExtraKey.SET_APN_MMS_PORT, apn.mmsPort)
        if (apn.authType != null)
            putInt(ExtraKey.SET_APN_AUTH_TYPE, apn.authType)
        if (apn.protocol != null)
            putInt(ExtraKey.SET_APN_PROTOCOL, apn.protocol)
        if (apn.roaming != null)
            putInt(ExtraKey.SET_APN_ROAMING, apn.roaming)
        if (apn.mvno != null)
            putInt(ExtraKey.SET_APN_MVNO, apn.mvno)
        if (apn.mvnoValue != null)
            putString(ExtraKey.SET_APN_MVNO_VALUE, apn.mvnoValue)
    }
}