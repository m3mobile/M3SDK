package net.m3mobile.sdk.keytool.api

import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

interface KeyApi {

    /**
     * Enables the Function (fn) key mode.
     *
     * @see disableFN
     * @see lockFN
     */
    @SupportedModels(DeviceModel.SL20K)
    fun enableFN()

    /**
     * Disables the Function (fn) key mode.
     *
     * @see enableFN
     * @see lockFN
     */
    @SupportedModels(DeviceModel.SL20K)
    fun disableFN()

    /**
     * Locks the Function (fn) key mode.
     *
     * @see enableFN
     * @see disableFN
     */
    @SupportedModels(DeviceModel.SL20K)
    fun lockFN()
}