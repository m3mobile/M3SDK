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
    @SupportedModels(
        DeviceModel.SL20,
        DeviceModel.SL20K,
        DeviceModel.SL20P,
        DeviceModel.SL25,
        DeviceModel.WD10,
        DeviceModel.SM24,
        DeviceModel.SM25
    )
    public fun setKeyFunction(key: String, function: String)

    /**
     * Sets the function and Wake-Up state for a key in one KeyTool request.
     *
     * The request is processed sequentially by KeyTool and is not transactional.
     *
     * @param key Key code to configure.
     * @param function Function name to be assigned.
     * @param wakeUpEnabled Whether the key wakes the device.
     */
    @SupportedModels(DeviceModel.SM24)
    public fun setKeyFunction(key: String, function: String, wakeUpEnabled: Boolean)

    /**
     * Enables the Home navigation button.
     *
     * This sends a one-way request to KeyTool. A normal return only confirms that
     * the request was sent; it does not confirm that SystemUI applied the setting.
     */
    @SupportedModels(DeviceModel.SM24, DeviceModel.SM25)
    public fun enableHomeButton()

    /** Disables the Home navigation button through KeyTool. */
    @SupportedModels(DeviceModel.SM24, DeviceModel.SM25)
    public fun disableHomeButton()

    /** Enables the Recent navigation button through KeyTool. */
    @SupportedModels(DeviceModel.SM24, DeviceModel.SM25)
    public fun enableRecentButton()

    /** Disables the Recent navigation button through KeyTool. */
    @SupportedModels(DeviceModel.SM24, DeviceModel.SM25)
    public fun disableRecentButton()
}
