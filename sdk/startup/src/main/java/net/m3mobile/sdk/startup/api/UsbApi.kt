package net.m3mobile.sdk.startup.api

interface UsbApi {

    /**
     * Sets the USB connection mode to MTP.
     */
    fun setUsbModeMtp()

    /**
     * Sets the USB connection mode to RNDIS.
     */
    fun setUsbModeRndis()

    /**
     * Sets the USB connection mode to MIDI.
     */
    fun setUsbModeMidi()

    /**
     * Sets the USB connection mode to PTP.
     */
    fun setUsbModePtp()

    /**
     * Disables all USB data connections, setting the mode to charging only.
     */
    fun setUsbModeNone()
}