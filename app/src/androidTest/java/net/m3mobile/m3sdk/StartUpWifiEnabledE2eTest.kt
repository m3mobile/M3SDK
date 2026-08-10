package net.m3mobile.m3sdk

import android.Manifest
import android.content.Context
import android.net.wifi.WifiManager
import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import net.m3mobile.sdk.M3Mobile
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartUpWifiEnabledE2eTest {

    @Test
    fun wifiStateCanBeChangedRepeatedAndRestoredThroughSdk() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.uiAutomation.adoptShellPermissionIdentity(Manifest.permission.ACCESS_WIFI_STATE)

        val wifiManager = instrumentation.targetContext.applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager
        val originalState = wifiManager.isWifiEnabled

        try {
            setWifiEnabledAndAwait(wifiManager, !originalState)
            setWifiEnabledAndAwait(wifiManager, originalState)
            setWifiEnabledAndAwait(wifiManager, originalState)
        } finally {
            if (!wifiStateReached(wifiManager, originalState)) {
                M3Mobile.instance.setWifiEnabled(originalState)
                awaitWifiState(wifiManager, originalState)
            }
            instrumentation.uiAutomation.dropShellPermissionIdentity()
        }
    }

    private fun setWifiEnabledAndAwait(wifiManager: WifiManager, enabled: Boolean) {
        M3Mobile.instance.setWifiEnabled(enabled)

        assertTrue(
            "Wi-Fi did not become ${if (enabled) "enabled" else "disabled"} within $STATE_CHANGE_TIMEOUT_MS ms",
            awaitWifiState(wifiManager, enabled),
        )
        SystemClock.sleep(STATE_SETTLE_DELAY_MS)
    }

    private fun awaitWifiState(wifiManager: WifiManager, enabled: Boolean): Boolean {
        val deadline = SystemClock.elapsedRealtime() + STATE_CHANGE_TIMEOUT_MS
        while (SystemClock.elapsedRealtime() < deadline) {
            if (wifiStateReached(wifiManager, enabled)) return true
            SystemClock.sleep(STATE_POLL_INTERVAL_MS)
        }
        return wifiStateReached(wifiManager, enabled)
    }

    private fun wifiStateReached(wifiManager: WifiManager, enabled: Boolean): Boolean {
        val expectedState = if (enabled) {
            WifiManager.WIFI_STATE_ENABLED
        } else {
            WifiManager.WIFI_STATE_DISABLED
        }
        return wifiManager.wifiState == expectedState
    }

    private companion object {
        const val STATE_CHANGE_TIMEOUT_MS = 15_000L
        const val STATE_POLL_INTERVAL_MS = 250L
        const val STATE_SETTLE_DELAY_MS = 3_000L
    }
}
