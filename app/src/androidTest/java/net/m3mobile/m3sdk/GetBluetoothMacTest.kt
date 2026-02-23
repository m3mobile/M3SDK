package net.m3mobile.m3sdk

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import net.m3mobile.core.RequestCallback
import net.m3mobile.sdk.M3Mobile
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assume
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class GetBluetoothMacTest {

    @Before
    fun assumeUL30() {
        Assume.assumeTrue(
            "This test only runs on UL30 devices",
            Build.MODEL.contains("UL30")
        )
    }

    @Test
    fun suspend_방식으로_블루투스_MAC_주소를_반환한다() = runBlocking {
        val mac = M3Mobile.instance.getBluetoothMac()
        assertTrue(mac.matches(Regex("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}")))
    }

    @Test
    fun callback_방식으로_블루투스_MAC_주소를_반환한다() {
        val latch = CountDownLatch(1)
        var result: String? = null
        var error: Exception? = null

        M3Mobile.instance.getBluetoothMac(RequestCallback { r, e ->
            result = r
            error = e
            latch.countDown()
        })

        assertTrue("Timeout waiting for callback", latch.await(5, TimeUnit.SECONDS))
        assertNull("Unexpected error: $error", error)
        assertNotNull(result)
        assertTrue(result!!.matches(Regex("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}")))
    }
}
