package net.m3mobile.feature.startup.api

import net.m3mobile.core.RequiresStartUp
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

public interface StartUpUsbApi {

    /**
     * Sets the USB connection mode to MTP.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    public fun setUsbModeMtp()

    /**
     * Sets the USB connection mode to RNDIS.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    public fun setUsbModeRndis()

    /**
     * Sets the USB connection mode to MIDI.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    public fun setUsbModeMidi()

    /**
     * Sets the USB connection mode to PTP.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    public fun setUsbModePtp()

    /**
     * Disables all USB data connections, setting the mode to charging only.
     *
     * StartUp version `6.5.10` or later is required.
     */
    @SupportedModels(DeviceModel.US20, DeviceModel.US30)
    @RequiresStartUp("6.5.10")
    public fun setUsbModeNone()

    /**
     * Blocks or allows SD card and USB storage mounting without changing the USB connection mode.
     *
     * Supported on all models with StartUp version `6.9.0` or later.
     *
     * @param blocked `true` blocks mounting and unmounts currently mounted target devices.
     * `false` allows mounting and attempts to mount connected, unmounted target devices.
     * @param resultAction Optional broadcast action for the asynchronous result. Null or empty
     * omits the result action. Register a receiver before calling this method to receive
     * `usb_success` (Boolean) and, on failure, `usb_error_message` (String).
     * Sending the request does not guarantee that the setting was applied successfully.
     */
    @RequiresStartUp("6.9.0")
    public fun setSdUsbStorageMountingBlocked(
        blocked: Boolean,
        resultAction: String? = null
    )
}
