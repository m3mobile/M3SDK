package net.m3mobile.sdk.startup.requester.network

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.FinishRequiredBroadcastRequester
import net.m3mobile.sdk.startup.api.Apn
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class SetApnRequester(
    override val context: Context,
    apn: Apn
) : FinishRequiredBroadcastRequester() {

    override val requestAction = RequestAction.CONFIG
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.APN
    override val extras = buildExtraBundle(apn)

    private fun buildExtraBundle(apn: Apn) = bundleOf(
        "apn_name" to apn.name,
        "apn_url" to apn.url,
        "apn_mcc" to apn.mcc,
        "apn_mnc" to apn.mnc,
        "apn_type" to apn.type
    ).apply {
        if (apn.proxy != null)
            putString("apn_proxy", apn.proxy)
        if (apn.port != null)
            putString("apn_port", apn.port)
        if (apn.user != null)
            putString("apn_user", apn.user)
        if (apn.password != null)
            putString("apn_server", apn.server)
        if (apn.mmsc != null)
            putString("apn_mmsc", apn.mmsc)
        if (apn.mmsProxy != null)
            putString("apn_mms_proxy", apn.mmsProxy)
        if (apn.mmsPort != null)
            putString("apn_mms_port", apn.mmsPort)
        if (apn.authType != null)
            putInt("apn_auth_type", apn.authType)
        if (apn.protocol != null)
            putInt("apn_protocol", apn.protocol)
        if (apn.roaming != null)
            putInt("apn_roaming", apn.roaming)
        if (apn.mvno != null)
            putInt("apn_mvno", apn.mvno)
        if (apn.mvnoValue != null)
            putString("apn_mvno_value", apn.mvnoValue)
    }
}