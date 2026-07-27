package net.m3mobile.feature.appcenter.internal

import android.content.Context
import android.content.Intent

internal fun interface AppCenterBroadcast {
    fun send(request: AppCenterBroadcastRequest)
}

internal class ExplicitAppCenterBroadcast(context: Context) : AppCenterBroadcast {

    private val context = context.applicationContext ?: context

    override fun send(request: AppCenterBroadcastRequest) {
        val intent = Intent(request.action).setPackage(request.packageName)

        request.extras().forEach { (key, value) ->
            when (value) {
                is String -> intent.putExtra(key, value)
                is Boolean -> intent.putExtra(key, value)
                is Int -> intent.putExtra(key, value)
                else -> throw IllegalArgumentException("Unsupported AppCenter extra type for $key.")
            }
        }

        context.sendBroadcast(intent)
    }
}
