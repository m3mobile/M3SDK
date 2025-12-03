package net.m3mobile.sdk.startup.requester.time

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal class SetNtpServerRequester(
    override val context: Context,
    host: String
): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.NTP
    override val extras = bundleOf("ntp_server" to host)
}