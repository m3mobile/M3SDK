package net.m3mobile.sdk.api

import android.content.Context
import android.content.res.Resources
import net.m3mobile.core.constants.DefaultValues
import net.m3mobile.core.utils.getGlobalString
import java.util.TimeZone
import kotlin.text.ifEmpty

public class TimeApiImpl(private val context: Context): TimeApi {

    override fun getNtpServer(): String {
        val server = context.getGlobalString("ntp_server")
        return server.ifEmpty { DefaultValues.NTP_SERVER }
    }

    override fun getNtpInterval(): Int {
        val intervalResId = Resources.getSystem()
            .getIdentifier(NAME_GET_NTP_INTERVAL, RESOURCE_TYPE_INTEGER, RESOURCE_PACKAGE)
        val interval = Resources.getSystem().getInteger(intervalResId)
        return interval
    }

    override fun getTimeZone(): String {
        return TimeZone.getDefault().id
    }

    private companion object {
        const val NAME_GET_NTP_INTERVAL = "config_ntpPollingInterval"
        const val RESOURCE_PACKAGE = "android"
        const val RESOURCE_TYPE_INTEGER = "integer"
    }
}