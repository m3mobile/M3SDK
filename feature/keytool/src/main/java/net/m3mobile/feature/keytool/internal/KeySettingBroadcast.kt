package net.m3mobile.feature.keytool.internal

import android.content.Context
import android.content.Intent

internal fun interface KeySettingBroadcast {
    fun send(request: KeySettingRequest)
}

internal class ExplicitKeySettingBroadcast(context: Context) : KeySettingBroadcast {

    private val context = context.applicationContext ?: context

    override fun send(request: KeySettingRequest) {
        val intent = Intent(request.action).setPackage(request.packageName)

        request.extras().forEach { (key, value) ->
            when (value) {
                is String -> intent.putExtra(key, value)
                is Boolean -> intent.putExtra(key, value)
                else -> throw IllegalArgumentException("Unsupported KeyTool extra type for $key.")
            }
        }

        if (request.ordered) {
            context.sendOrderedBroadcast(intent, null)
        } else {
            context.sendBroadcast(intent)
        }
    }
}
