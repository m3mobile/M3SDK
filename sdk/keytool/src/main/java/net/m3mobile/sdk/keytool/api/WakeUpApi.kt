package net.m3mobile.sdk.keytool.api

import net.m3mobile.core.device.DeviceModel
import net.m3mobile.core.SupportedModels

public interface WakeUpApi {

    /**
     * Enables the device wake-up feature when the left scan key is pressed.
     */
    @SupportedModels(DeviceModel.SL20P)
    public fun enableLeftScanWakeUp()

    /**
     * Disables the device wake-up feature when the left scan key is pressed.
     */
    @SupportedModels(DeviceModel.SL20P)
    public fun disableLeftScanWakeUp()

    /**
     * Enables the device wake-up feature when the right scan key is pressed.
     */
    @SupportedModels(DeviceModel.SL20P)
    public fun enableRightScanWakeUp()

    /**
     * Disables the device wake-up feature when the right scan key is pressed.
     */
    @SupportedModels(DeviceModel.SL20P)
    public fun disableRightScanWakeUp()
}