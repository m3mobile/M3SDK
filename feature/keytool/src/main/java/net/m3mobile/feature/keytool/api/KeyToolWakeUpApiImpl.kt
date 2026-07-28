@file:OptIn(net.m3mobile.core.InternalM3Api::class)

package net.m3mobile.feature.keytool.api

import android.content.Context
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.core.device.currentDeviceModel
import net.m3mobile.feature.keytool.KeyToolAppGuard
import net.m3mobile.feature.keytool.constants.KeyToolContract
import net.m3mobile.feature.keytool.internal.ExplicitKeySettingBroadcast
import net.m3mobile.feature.keytool.internal.KeySettingRequestFactory
import net.m3mobile.feature.keytool.internal.KeyToolVersionPolicy

internal class KeyToolWakeUpApiImpl(private val context: Context): KeyToolWakeUpApi {

    private val appGuard = KeyToolAppGuard(context)
    private val broadcast = ExplicitKeySettingBroadcast(context)

    override fun enableLeftScanWakeUp() {
        setScanWakeUp("enableLeftScanWakeUp", KeyToolContract.LEFT_SCAN, true)
    }

    override fun disableLeftScanWakeUp() {
        setScanWakeUp("disableLeftScanWakeUp", KeyToolContract.LEFT_SCAN, false)
    }

    override fun enableRightScanWakeUp() {
        setScanWakeUp("enableRightScanWakeUp", KeyToolContract.RIGHT_SCAN, true)
    }

    override fun disableRightScanWakeUp() {
        setScanWakeUp("disableRightScanWakeUp", KeyToolContract.RIGHT_SCAN, false)
    }

    private fun setScanWakeUp(methodName: String, keyName: String, enabled: Boolean) {
        val model = currentDeviceModel
        if (model == DeviceModel.SM24) {
            appGuard.assertSl20AppVersion(
                methodName,
                model,
                KeyToolVersionPolicy.SCAN_WAKE_UP_MINIMUM_VERSION,
            )
        } else {
            appGuard.assertLegacyWakeUpAppAvailable(methodName)
        }

        broadcast.send(
            KeySettingRequestFactory.scanWakeUpRequest(model, keyName, enabled)
        )
    }
}
