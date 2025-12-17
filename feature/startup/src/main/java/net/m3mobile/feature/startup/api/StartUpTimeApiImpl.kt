package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.time.SetDateTimeRequester
import net.m3mobile.feature.startup.requester.time.SetNtpServerRequester
import net.m3mobile.feature.startup.requester.time.SetTimezoneRequester
import java.time.LocalDateTime

internal class StartUpTimeApiImpl(private val context: Context): StartUpTimeApi {

    override fun setDateTime(dateTime: LocalDateTime) {
        SetDateTimeRequester(context, dateTime).request()
    }

    override fun setNtpServer(host: String) {
        SetNtpServerRequester(context, host).request()
    }

    override fun setTimeZone(timezone: String) {
        SetTimezoneRequester(context, timezone).request()
    }
}