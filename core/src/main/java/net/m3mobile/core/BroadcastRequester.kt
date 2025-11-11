package net.m3mobile.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf

@InternalM3Api
abstract class BroadcastRequester {

    protected abstract val requestAction: String
    protected abstract val typeKey: String
    protected abstract val typeValue: String
    protected open val extras: Bundle = bundleOf()
    protected abstract val context: Context

    fun runBroadcast() {
        val intent = Intent(requestAction)
            .putExtras(extras)
            .putExtra(typeKey, typeValue)
        context.sendBroadcast(intent)
    }
}