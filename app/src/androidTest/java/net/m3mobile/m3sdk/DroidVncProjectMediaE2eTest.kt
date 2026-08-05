package net.m3mobile.m3sdk

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import net.m3mobile.feature.startup.params.ProjectMediaResult
import net.m3mobile.feature.startup.params.ProjectMediaStatus
import net.m3mobile.sdk.M3Mobile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DroidVncProjectMediaE2eTest {
    @Test
    fun customerAppAllowsDroidVncProjectMediaThroughStartUp() {
        assertTrue("This E2E test requires SM24", Build.MODEL.contains("SM24"))

        val latch = CountDownLatch(1)
        val callbackCount = AtomicInteger(0)
        var result: ProjectMediaResult? = null
        var error: Exception? = null
        val targetPackage = InstrumentationRegistry.getArguments()
            .getString(TARGET_PACKAGE_ARGUMENT)
            ?: DROID_VNC_PACKAGE

        M3Mobile.instance.allowProjectMedia(targetPackage) { value, exception ->
            result = value
            error = exception
            callbackCount.incrementAndGet()
            latch.countDown()
        }

        assertTrue("Timeout waiting for StartUp callback", latch.await(10, TimeUnit.SECONDS))
        assertNull("Unexpected communication error: $error", error)
        assertNotNull("StartUp returned no result", result)
        assertEquals(ProjectMediaStatus.SUCCESS, result?.status)
        assertTrue("PROJECT_MEDIA result was not successful: $result", result?.successful == true)
        assertEquals("", result?.errorMessage)

        Thread.sleep(500)
        assertEquals("Callback must complete exactly once", 1, callbackCount.get())
    }

    private companion object {
        const val TARGET_PACKAGE_ARGUMENT = "targetPackage"
        const val DROID_VNC_PACKAGE = "net.christianbeier.droidvnc_ng"
    }
}
