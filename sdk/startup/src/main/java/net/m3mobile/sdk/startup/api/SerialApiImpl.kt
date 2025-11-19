package net.m3mobile.sdk.startup.api

import android.content.Context
import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.launchOnMain
import net.m3mobile.sdk.startup.requester.serial.GetSerialNumberRequester

internal class SerialApiImpl(private val context: Context): SerialApi {

    override suspend fun getSerialNumber(): String {
        return GetSerialNumberRequester(context = context).fetch()
    }

    override fun getSerialNumber(callback: RequestCallback<String>): Job {
        return launchOnMain {
            try {
                val serial = getSerialNumber()
                callback.onComplete(serial, null)
            } catch (e: Exception) {
                callback.onComplete(null, e)
            }
        }
    }
}