package net.m3mobile.sdk.startup.api

import android.content.Context
import android.os.Build
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.time.SetDateTimeRequester
import net.m3mobile.sdk.startup.requester.time.SetNtpServerRequester
import net.m3mobile.sdk.startup.requester.time.SetTimezoneRequester
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class TimeApiImpl(private val context: Context): TimeApi {

    override fun setDateTime(dateTime: LocalDateTime) {
        SetDateTimeRequester(context, dateTime).request()
    }

    override fun setNtpServer(host: String) {
        SetNtpServerRequester(context, bundleOf("ntp_server" to host)).request()
    }

    override fun setTimezone(timezone: String) {
        SetTimezoneRequester(context, bundleOf("timezone" to timezone)).request()
    }
}