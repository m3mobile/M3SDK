package net.m3mobile.sdk.startup.api

import net.m3mobile.core.RequiresStartUp
import net.m3mobile.sdk.startup.params.Apn

public interface NetworkApi {

    /**
     * Sets the Access Point Name (APN) configuration for the network.
     *
     * This function configures the device's network settings using the provided [Apn] object.
     * It is used to define connection details such as the APN name, URL, proxy, port,
     * and authentication credentials required for establishing a mobile data connection.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param apn The [Apn] object containing the network configuration details.
     *            Use `Apn.builder()` to construct a valid instance.
     */
    @RequiresStartUp("6.2.14")
    public fun setApn(apn: Apn)
}