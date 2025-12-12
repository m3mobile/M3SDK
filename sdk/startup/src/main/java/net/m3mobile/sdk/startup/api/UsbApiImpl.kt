package net.m3mobile.sdk.startup.api

import android.content.Context
import android.content.IntentFilter
import net.m3mobile.sdk.startup.constants.ResponseAction
import net.m3mobile.sdk.startup.requester.usb.SetUsbModeMidiRequester
import net.m3mobile.sdk.startup.requester.usb.SetUsbModeMtpRequester
import net.m3mobile.sdk.startup.requester.usb.SetUsbModeNoneRequester
import net.m3mobile.sdk.startup.requester.usb.SetUsbModePtpRequester
import net.m3mobile.sdk.startup.requester.usb.SetUsbModeRndisRequester

internal class UsbApiImpl(private val context: Context): UsbApi {

    override fun setUsbModeMtp() {
        SetUsbModeMtpRequester(context).request()
    }

    override fun setUsbModeRndis() {
        SetUsbModeRndisRequester(context).request()
    }

    override fun setUsbModeMidi() {
        SetUsbModeMidiRequester(context).request()
    }

    override fun setUsbModePtp() {
        SetUsbModePtpRequester(context).request()
    }

    override fun setUsbModeNone() {
        SetUsbModeNoneRequester(context).request()
    }

    override fun getCurrentUsbModes(): List<String> {
        val intent = context.registerReceiver(null,
            IntentFilter(ResponseAction.USB_STATE)
        )

        return intent?.run {
            listOf("mtp", "ptp", "midi", "rndis", "ncm", "adb").filter {
                getBooleanExtra(it, false)
            }
        } ?: emptyList()
    }
}