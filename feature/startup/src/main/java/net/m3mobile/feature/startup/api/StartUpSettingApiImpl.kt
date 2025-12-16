package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.startup.ResetStartUpSettingRequester

internal class StartUpSettingApiImpl(private val context: Context) : StartUpSettingApi {

    override fun resetStartUpSetting() {
        ResetStartUpSettingRequester(context).request()
    }
}