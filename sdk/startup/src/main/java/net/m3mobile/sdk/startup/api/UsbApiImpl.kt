package net.m3mobile.sdk.startup.api

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.usb.SetUsbModeRequester

class UsbApiImpl(private val context: Context): UsbApi {

    override fun setUsbMode(mode: UsbMode) {
        SetUsbModeRequester(context, bundleOf("usb_mode" to mode.name.lowercase()))
            .runBroadcast()
    }
}

/**
 * An enum class to define the USB connection mode of the device.
 * Used with [UsbApi.setUsbMode] to configure how the device behaves when connected via USB.
 */
enum class UsbMode {
    /** Media Transfer Protocol */
    MTP,

    /**
     * Remote NDIS (RNDIS) mode.
     * Functions as a virtual Ethernet link. Typically used for tethering or network access.
     */
    RNDIS,

    /** Musical Instrument Digital Interface */
    MIDI,

    /** Picture Transfer Protocol */
    PTP,

    /** Disable USB function */
    NONE
}