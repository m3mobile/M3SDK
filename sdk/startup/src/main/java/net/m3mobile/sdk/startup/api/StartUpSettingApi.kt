package net.m3mobile.sdk.startup.api

import net.m3mobile.core.RequiresStartUp

interface StartUpSettingApi {

    /**
     * Resets the StartUp settings to default values.
     *
     * StartUp version `6.2.14` or later is required.
     */
    @RequiresStartUp("6.2.14")
    fun resetStartUpSetting()
}