package net.m3mobile.feature.startup.requester.permission

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Messenger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue
import net.m3mobile.feature.startup.params.ProjectMediaResult
import net.m3mobile.feature.startup.params.projectMediaResult
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class ProjectMediaRequest(
    private val context: Context,
    private val packageName: String,
) {
    suspend fun result(): ProjectMediaResult =
        withTimeout(TIMEOUT_MILLIS) {
            suspendCancellableCoroutine { continuation ->
                val messenger = Messenger(
                    Handler(Looper.getMainLooper()) { message ->
                        if (!continuation.isActive) return@Handler true

                        continuation.resume(
                            projectMediaResult(
                                message.what,
                                message.data.getString(ExtraKey.PROJECT_MEDIA_ERROR_MESSAGE),
                            ),
                        )
                        true
                    },
                )

                val intent = Intent(RequestAction.SYSTEM)
                    .setPackage(STARTUP_PACKAGE_NAME)
                    .putExtra(TypeKey.SETTING, TypeValue.PROJECT_MEDIA)
                    .putExtra(ExtraKey.PROJECT_MEDIA_PACKAGE, packageName)
                    .putExtra(ExtraKey.PROJECT_MEDIA_MESSENGER, messenger)

                try {
                    context.sendBroadcast(intent)
                } catch (e: Exception) {
                    if (continuation.isActive) continuation.resumeWithException(e)
                }
            }
        }

    private companion object {
        const val STARTUP_PACKAGE_NAME = "com.m3.startup"
        const val TIMEOUT_MILLIS = 3_000L
    }
}
