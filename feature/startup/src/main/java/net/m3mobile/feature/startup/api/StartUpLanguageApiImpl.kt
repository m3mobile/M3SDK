package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.language.SetLanguageRequester

internal class StartUpLanguageApiImpl(private val context: Context): StartUpLanguageApi {

    override fun setLanguage(language: String, country: String) {
        SetLanguageRequester(context, language, country).request()
    }
}