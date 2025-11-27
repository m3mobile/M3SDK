package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.requester.startup.ResetStartUpSettingRequester

internal class StartUpSettingApiImpl(private val context: Context) : StartUpSettingApi {

    override fun resetStartUpSetting() {
        ResetStartUpSettingRequester(context).request()
    }
}