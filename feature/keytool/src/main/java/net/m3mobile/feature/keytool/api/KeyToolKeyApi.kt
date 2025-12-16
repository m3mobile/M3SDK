package net.m3mobile.feature.keytool.api

import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel

public interface KeyToolKeyApi {

    /**
     * Enables the Function (fn) key mode.
     *
     * @see disableFN
     * @see lockFN
     */
    @SupportedModels(DeviceModel.SL20K)
    public fun enableFN()

    /**
     * Disables the Function (fn) key mode.
     *
     * @see enableFN
     * @see lockFN
     */
    @SupportedModels(DeviceModel.SL20K)
    public fun disableFN()

    /**
     * Locks the Function (fn) key mode.
     *
     * @see enableFN
     * @see disableFN
     */
    @SupportedModels(DeviceModel.SL20K)
    public fun lockFN()

    /**
     * Sets the specific function for a key.
     *
     * @param key Key code to set the function
     * @param function Function name to be assigned.
     * Please check the [link](https://github.com/m3mobile/Android-Library-M3SDK/blob/master/docs/keytool/sl-series-key-setting-sdk-korean.md)
     * to see the list of assignable functions.
     * @throws IllegalArgumentException If the key or function is incorrect
     */
    @SupportedModels(DeviceModel.SL20, DeviceModel.SL20K, DeviceModel.SL20P)
    public fun setKeyFunction(key: String, function: String)
}