package net.m3mobile.sdk.startup.api

import net.m3mobile.core.RequiresStartUp
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

interface UsbApi {

    /**
     * Sets the USB connection mode to MTP.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    fun setUsbModeMtp()

    /**
     * Sets the USB connection mode to RNDIS.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    fun setUsbModeRndis()

    /**
     * Sets the USB connection mode to MIDI.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    fun setUsbModeMidi()

    /**
     * Sets the USB connection mode to PTP.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    fun setUsbModePtp()

    /**
     * Disables all USB data connections, setting the mode to charging only.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    fun setUsbModeNone()
}