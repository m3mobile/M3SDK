package net.m3mobile.sdk.api

public interface UsbApi {

    /**
     * Retrieves the current USB connection mode.
     *
     * Returns a list containing the currently active USB mode strings.
     *
     * @return A list of strings representing the active USB modes. Empty if no active USB modes
     */
    public fun getCurrentUsbModes(): List<String>
}