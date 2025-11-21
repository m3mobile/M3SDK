package net.m3mobile.sdk.startup.api

interface NetworkApi {

    /**
     * Sets the Access Point Name (APN) configuration for the network.
     *
     * This function configures the device's network settings using the provided [Apn] object.
     * It is used to define connection details such as the APN name, URL, proxy, port,
     * and authentication credentials required for establishing a mobile data connection.
     *
     * @param apn The [Apn] object containing the network configuration details.
     *            Use `Apn.builder()` to construct a valid instance.
     */
    fun setApn(apn: Apn)
}