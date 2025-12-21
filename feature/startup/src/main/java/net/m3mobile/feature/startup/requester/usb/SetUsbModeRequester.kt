package net.m3mobile.feature.startup.requester.usb

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.ExtraValue
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal abstract class SetUsbModeRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.USB_SETTING
    override val extras
        get() = bundleOf(ExtraKey.SET_USB_MODE to mode)
    abstract val mode: String
}

internal class SetUsbModeMtpRequester(
    override val context: Context
): SetUsbModeRequester() {

    override val mode = ExtraValue.USB_MODE_MTP
}

internal class SetUsbModeRndisRequester(
    override val context: Context
): SetUsbModeRequester() {

    override val mode = ExtraValue.USB_MODE_RNDIS
}

internal class SetUsbModeMidiRequester(
    override val context: Context
): SetUsbModeRequester() {

    override val mode = ExtraValue.USB_MODE_MIDI
}

internal class SetUsbModePtpRequester(
    override val context: Context
): SetUsbModeRequester() {

    override val mode = ExtraValue.USB_MODE_PTP
}

internal class SetUsbModeNoneRequester(
    override val context: Context
): SetUsbModeRequester() {

    override val mode = ExtraValue.USB_MODE_NONE
}