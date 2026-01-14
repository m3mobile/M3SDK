package net.m3mobile.feature.startup.api

import net.m3mobile.core.RequiresStartUp

public interface StartUpLanguageApi {

    /**
     * Sets the device's system language.
     *
     * @param language The language code to set (e.g., "en" for English, "ko" for Korean).
     * @param country The country code to set (e.g., "US" for United States, "KR" for Korea).
     */
    @RequiresStartUp("6.2.14")
    public fun setLanguage(language: String, country: String)
}