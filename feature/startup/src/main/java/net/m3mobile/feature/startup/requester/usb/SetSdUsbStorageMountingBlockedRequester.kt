package net.m3mobile.feature.startup.requester.usb

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal class SetSdUsbStorageMountingBlockedRequester(
    override val context: Context,
    private val blocked: Boolean,
    private val resultAction: String? = null
): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.USB_SETTING
    override val extras
        get() = bundleOf(
            ExtraKey.BLOCK_SD_USB_STORAGE_MOUNTING to blocked
        ).apply {
            if (!resultAction.isNullOrEmpty()) {
                putString(ExtraKey.USB_RESULT_ACTION, resultAction)
            }
        }
}
