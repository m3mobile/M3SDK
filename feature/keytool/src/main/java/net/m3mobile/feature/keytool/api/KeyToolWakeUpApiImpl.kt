package net.m3mobile.feature.keytool.api

import android.content.Context
import net.m3mobile.feature.keytool.requester.wakeup.DisableLeftScanWakeUpRequester
import net.m3mobile.feature.keytool.requester.wakeup.DisableRightScanWakeUpRequester
import net.m3mobile.feature.keytool.requester.wakeup.EnableLeftScanWakeUpRequester
import net.m3mobile.feature.keytool.requester.wakeup.EnableRightScanWakeUpRequester

internal class KeyToolWakeUpApiImpl(private val context: Context): KeyToolWakeUpApi {

    override fun enableLeftScanWakeUp() {
        EnableLeftScanWakeUpRequester(context).request()
    }

    override fun disableLeftScanWakeUp() {
        DisableLeftScanWakeUpRequester(context).request()
    }

    override fun enableRightScanWakeUp() {
        EnableRightScanWakeUpRequester(context).request()
    }

    override fun disableRightScanWakeUp() {
        DisableRightScanWakeUpRequester(context).request()
    }
}