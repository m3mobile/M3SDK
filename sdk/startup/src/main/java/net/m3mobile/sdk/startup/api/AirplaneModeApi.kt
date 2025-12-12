package net.m3mobile.sdk.startup.api

import net.m3mobile.core.RequiresStartUp

public interface AirplaneModeApi {

    /**
     * Turns on airplane mode.
     *
     * StartUp version `6.3.7` or later is required.
     */
    @RequiresStartUp("6.3.7")
    public fun turnOnAirplaneMode()

    /**
     * Turns off airplane mode.
     *
     * StartUp version `6.3.7` or later is required.
     */
    @RequiresStartUp("6.3.7")
    public fun turnOffAirplaneMode()
}