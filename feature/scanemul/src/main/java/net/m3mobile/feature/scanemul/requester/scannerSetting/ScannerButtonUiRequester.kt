package net.m3mobile.feature.scanemul.requester.scannerSetting

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import net.m3mobile.feature.scanemul.constants.ExtraKey
import net.m3mobile.feature.scanemul.constants.RequestAction
import net.m3mobile.feature.scanemul.constants.ResponseAction
import net.m3mobile.feature.scanemul.constants.TypeKey
import net.m3mobile.feature.scanemul.constants.TypeValue
import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions
import net.m3mobile.feature.scanemul.params.ScannerButtonUiResult
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSettings
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSize
import net.m3mobile.feature.scanemul.params.ScannerButtonUiStatus
import net.m3mobile.feature.scanemul.params.ScannerButtonUiTransportStatus

internal class ScannerButtonUiRequester(
    context: Context,
    private val requestId: String,
    private val options: ScannerButtonUiOptions?,
    private val timeoutMillis: Long = DEFAULT_TIMEOUT_MILLIS,
) {
    private val context: Context = context.applicationContext
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var dynamicResult: ScannerButtonUiResult? = null

    suspend fun fetch(): ScannerButtonUiResult {
        if (options != null && !options.valid()) {
            return transportResult(ScannerButtonUiTransportStatus.INVALID_SDK_REQUEST)
        }
        if (!scanEmulInstalled()) {
            return transportResult(ScannerButtonUiTransportStatus.SCANEMUL_NOT_INSTALLED)
        }

        return suspendCancellableCoroutine { continuation ->
            val completed = AtomicBoolean(false)
            var registered = false
            lateinit var timeout: Runnable
            lateinit var dynamicReceiver: BroadcastReceiver

            fun cleanup() {
                handler.removeCallbacks(timeout)
                if (registered) {
                    try {
                        context.unregisterReceiver(dynamicReceiver)
                    } catch (_: IllegalArgumentException) {
                    }
                    registered = false
                }
            }

            fun complete(result: ScannerButtonUiResult) {
                if (!completed.compareAndSet(false, true)) return
                cleanup()
                continuation.resume(result)
            }

            timeout = Runnable {
                complete(
                    dynamicResult ?: transportResult(ScannerButtonUiTransportStatus.TIMEOUT),
                )
            }

            dynamicReceiver = object : BroadcastReceiver() {
                override fun onReceive(receiverContext: Context?, intent: Intent?) {
                    val result = result(intent ?: return) ?: return
                    dynamicResult = result
                }
            }

            val finalReceiver = object : BroadcastReceiver() {
                override fun onReceive(receiverContext: Context?, intent: Intent?) {
                    val result = getResultExtras(false)?.let(::result)
                    if (result != null) {
                        complete(result)
                        return
                    }
                    complete(
                        dynamicResult
                            ?: transportResult(ScannerButtonUiTransportStatus.FEATURE_NOT_AVAILABLE),
                    )
                }
            }

            continuation.invokeOnCancellation { cleanup() }

            try {
                registerDynamicReceiver(dynamicReceiver)
                registered = true
                handler.postDelayed(timeout, timeoutMillis)
                context.sendOrderedBroadcast(
                    requestIntent(),
                    null,
                    finalReceiver,
                    handler,
                    Activity.RESULT_CANCELED,
                    null,
                    null,
                )
            } catch (_: RuntimeException) {
                complete(transportResult(ScannerButtonUiTransportStatus.SEND_FAILED))
            }
        }
    }

    private fun requestIntent(): Intent {
        val action = if (options == null) {
            RequestAction.GET_SCANNER_SETTING
        } else {
            RequestAction.SET_SCANNER_SETTING
        }
        val intent = Intent(action)
            .setPackage(SCANEMUL_PACKAGE)
            .putExtra(TypeKey.SETTING, TypeValue.SCANNER_BUTTON_UI)
            .putExtra(ExtraKey.REQUEST_ID, requestId)

        options?.imagePath()?.let {
            intent.putExtra(ExtraKey.SCANNER_BUTTON_IMAGE_PATH, it)
        }
        options?.opacityPercent()?.let {
            intent.putExtra(ExtraKey.SCANNER_BUTTON_OPACITY_PERCENT, it)
        }
        options?.size()?.let {
            intent.putExtra(ExtraKey.SCANNER_BUTTON_SIZE, it.value())
        }

        return intent
    }

    private fun registerDynamicReceiver(receiver: BroadcastReceiver) {
        val filter = IntentFilter(ResponseAction.GET_SCANNER_SETTING)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            context.registerReceiver(receiver, filter)
        }
    }

    private fun scanEmulInstalled(): Boolean =
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    SCANEMUL_PACKAGE,
                    PackageManager.PackageInfoFlags.of(0),
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(SCANEMUL_PACKAGE, 0)
            }
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        } catch (_: RuntimeException) {
            false
        }

    private fun result(intent: Intent): ScannerButtonUiResult? {
        if (intent.action != ResponseAction.GET_SCANNER_SETTING) return null
        if (stringExtra(intent.extras, TypeKey.SETTING) != TypeValue.SCANNER_BUTTON_UI) return null
        return result(intent.extras ?: return null)
    }

    private fun result(extras: Bundle): ScannerButtonUiResult? {
        val responseRequestId = stringExtra(extras, ExtraKey.REQUEST_ID)
        if (responseRequestId != requestId) return null
        val rawStatus = stringExtra(extras, ExtraKey.STATUS)
        val settings = settings(extras) ?: return null
        return ScannerButtonUiResult(
            requestId,
            ScannerButtonUiTransportStatus.OK,
            extras.getBoolean(ExtraKey.SUCCESS, false),
            ScannerButtonUiStatus.byValue(rawStatus),
            rawStatus,
            extras.getBoolean(ExtraKey.RUNTIME_APPLIED, false),
            settings,
        )
    }

    private fun settings(extras: Bundle): ScannerButtonUiSettings? {
        val imagePath = stringExtra(extras, ExtraKey.SCANNER_BUTTON_IMAGE_PATH) ?: return null
        val opacity = intExtra(extras, ExtraKey.SCANNER_BUTTON_OPACITY_PERCENT) ?: return null
        val size = ScannerButtonUiSize.byValue(
            stringExtra(extras, ExtraKey.SCANNER_BUTTON_SIZE),
        )
        return ScannerButtonUiSettings(imagePath, opacity, size)
    }

    @Suppress("DEPRECATION")
    private fun stringExtra(extras: Bundle?, key: String): String? =
        try {
            extras?.get(key) as? String
        } catch (_: RuntimeException) {
            null
        }

    @Suppress("DEPRECATION")
    private fun intExtra(extras: Bundle, key: String): Int? =
        try {
            extras.get(key) as? Int
        } catch (_: RuntimeException) {
            null
        }

    private fun transportResult(
        status: ScannerButtonUiTransportStatus,
    ): ScannerButtonUiResult =
        ScannerButtonUiResult(
            requestId,
            status,
            false,
            ScannerButtonUiStatus.UNKNOWN,
            null,
            false,
            null,
        )

    internal companion object {
        private const val SCANEMUL_PACKAGE = "net.m3mobile.app.scanemul"
        private const val DEFAULT_TIMEOUT_MILLIS = 3000L

        fun requestId(): String = UUID.randomUUID().toString()
    }
}
