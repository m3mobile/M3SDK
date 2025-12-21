package net.m3mobile.sdk.api

import android.content.Context
import android.content.IntentFilter

public class UsbApiImpl(private val context: Context): UsbApi {

    override fun getCurrentUsbModes(): List<String> {
        val intent = context.registerReceiver(null,
            IntentFilter("android.hardware.usb.action.USB_STATE")
        )

        return intent?.run {
            listOf("mtp", "ptp", "midi", "rndis", "ncm", "adb").filter {
                getBooleanExtra(it, false)
            }
        } ?: emptyList()
    }
}