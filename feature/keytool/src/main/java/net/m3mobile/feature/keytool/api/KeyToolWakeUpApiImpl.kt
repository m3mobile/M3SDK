package net.m3mobile.feature.keytool.api

import android.content.Context
import net.m3mobile.feature.keytool.KeyToolAppGuard
import net.m3mobile.feature.keytool.requester.wakeup.DisableLeftScanWakeUpRequester
import net.m3mobile.feature.keytool.requester.wakeup.DisableRightScanWakeUpRequester
import net.m3mobile.feature.keytool.requester.wakeup.EnableLeftScanWakeUpRequester
import net.m3mobile.feature.keytool.requester.wakeup.EnableRightScanWakeUpRequester

internal class KeyToolWakeUpApiImpl(private val context: Context): KeyToolWakeUpApi {

    private val appGuard = KeyToolAppGuard(context)

    override fun enableLeftScanWakeUp() {
        appGuard.assertWakeUpAppAvailable("enableLeftScanWakeUp")
        EnableLeftScanWakeUpRequester(context).request()
    }

    override fun disableLeftScanWakeUp() {
        appGuard.assertWakeUpAppAvailable("disableLeftScanWakeUp")
        DisableLeftScanWakeUpRequester(context).request()
    }

    override fun enableRightScanWakeUp() {
        appGuard.assertWakeUpAppAvailable("enableRightScanWakeUp")
        EnableRightScanWakeUpRequester(context).request()
    }

    override fun disableRightScanWakeUp() {
        appGuard.assertWakeUpAppAvailable("disableRightScanWakeUp")
        DisableRightScanWakeUpRequester(context).request()
    }
}