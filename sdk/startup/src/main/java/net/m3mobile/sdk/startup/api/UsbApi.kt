package net.m3mobile.sdk.startup.api

import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

interface UsbApi {

    /**
     * Sets the USB connection mode to MTP.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun setUsbModeMtp()

    /**
     * Sets the USB connection mode to RNDIS.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun setUsbModeRndis()

    /**
     * Sets the USB connection mode to MIDI.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun setUsbModeMidi()

    /**
     * Sets the USB connection mode to PTP.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun setUsbModePtp()

    /**
     * Disables all USB data connections, setting the mode to charging only.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    fun setUsbModeNone()
}