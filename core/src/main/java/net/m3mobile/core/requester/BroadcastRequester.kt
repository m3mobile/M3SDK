package net.m3mobile.core.requester

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import net.m3mobile.core.InternalM3Api

@InternalM3Api
abstract class BroadcastRequester {

    protected abstract val requestAction: String
    protected open val typeKey: String = ""
    protected open val typeValue: String = ""
    protected open val extras: Bundle = bundleOf()
    protected abstract val context: Context

    fun runBroadcast() {
        val intent = Intent(requestAction)

        if (!extras.isEmpty)
            intent.putExtras(extras)

        if(typeKey.isNotEmpty()) {
            intent.putExtra(typeKey, typeValue)
        }
        context.sendBroadcast(intent)
    }
}