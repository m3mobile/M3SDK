package net.m3mobile.core.requester

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import net.m3mobile.core.InternalM3Api
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 브로드캐스트 요청을 보내고, 응답을 받기 위한 기본 클래스입니다.
 *
 * [참고](https://github.com/m3mobile/M3SDK/blob/main/docs/How_to_add_new_api.md)
 */
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