package net.m3mobile.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@InternalM3Api
abstract class AwaitableBroadcastRequester<T: Any>: BroadcastRequester() {

    protected abstract val responseAction: String
    protected open val timeoutMillis = 3000L

    suspend fun requestResult(): T {
        return withTimeout(timeoutMillis) {
            suspendCancellableCoroutine { continuation ->
                val receiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        if (intent?.action == responseAction) {
                            this@AwaitableBroadcastRequester.context.unregisterReceiver(this)

                            try {
                                val data = getExtra(intent)
                                    ?: throw NoSuchElementException("Failed to get extra '$typeKey' or type mismatch.")
                                continuation.resume(data)
                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    }
                }

                val filter = IntentFilter(responseAction)
                ContextCompat.registerReceiver(context, receiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)

                runBroadcast()

                continuation.invokeOnCancellation {
                    this@AwaitableBroadcastRequester.context.unregisterReceiver(receiver)
                }
            }
        }
    }

    protected abstract fun getExtra(intent: Intent): T?
}