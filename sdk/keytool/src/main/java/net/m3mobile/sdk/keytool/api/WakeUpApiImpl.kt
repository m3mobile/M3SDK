package net.m3mobile.sdk.keytool.api

import android.content.Context
import net.m3mobile.sdk.keytool.requester.wakeup.DisableLeftScanWakeUpRequester
import net.m3mobile.sdk.keytool.requester.wakeup.DisableRightScanWakeUpRequester
import net.m3mobile.sdk.keytool.requester.wakeup.EnableLeftScanWakeUpRequester
import net.m3mobile.sdk.keytool.requester.wakeup.EnableRightScanWakeUpRequester

internal class WakeUpApiImpl(private val context: Context): WakeUpApi {

    override fun enableLeftScanWakeUp() {
        EnableLeftScanWakeUpRequester(context).runBroadcast()
    }

    override fun disableLeftScanWakeUp() {
        DisableLeftScanWakeUpRequester(context).runBroadcast()
    }

    override fun enableRightScanWakeUp() {
        EnableRightScanWakeUpRequester(context).runBroadcast()
    }

    override fun disableRightScanWakeUp() {
        DisableRightScanWakeUpRequester(context).runBroadcast()
    }
}