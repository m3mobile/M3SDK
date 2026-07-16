package net.m3mobile.samples.compose

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class ExecutionTrace {
    private var attempt = 0
    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

    @Synchronized
    internal fun message(operation: String, body: String): String {
        attempt += 1
        return buildString {
            appendLine("operation=$operation")
            appendLine("attempt=$attempt")
            appendLine("at=${timeFormat.format(Date())}")
            append(body)
        }
    }
}
