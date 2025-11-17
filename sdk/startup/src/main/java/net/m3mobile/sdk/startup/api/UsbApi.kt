package net.m3mobile.sdk.startup.api

interface UsbApi {

    /**
     * Sets the USB connection mode for the device.
     *
     * This function switches the device's USB behavior, for example,
     * to MTP (Media Transfer Protocol) or charging only.
     *
     * @param mode The desired [UsbMode] to activate.
     */
    fun setUsbMode(mode: UsbMode)
}