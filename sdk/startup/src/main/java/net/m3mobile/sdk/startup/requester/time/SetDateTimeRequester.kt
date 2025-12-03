package net.m3mobile.sdk.startup.requester.time

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

internal class SetDateTimeRequester(
    override val context: Context,
    private val dateTime: LocalDateTime
): BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.DATETIME

    override fun request() {
        val dateString =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .format(dateTime)
            else SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateTime)
        val timeString =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) DateTimeFormatter.ofPattern("HH:mm:ss")
                .format(dateTime)
            else SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(dateTime)

        extras.putString("date", dateString)
        extras.putString("time", timeString)
        runBroadcast()
    }
}