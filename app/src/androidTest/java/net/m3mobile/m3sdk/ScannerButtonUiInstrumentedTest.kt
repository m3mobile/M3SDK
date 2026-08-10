package net.m3mobile.m3sdk

import android.os.Build
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions
import net.m3mobile.feature.scanemul.params.ScannerButtonUiResult
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSettings
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSize
import net.m3mobile.feature.scanemul.params.ScannerButtonUiStatus
import net.m3mobile.feature.scanemul.params.ScannerButtonUiTransportStatus
import net.m3mobile.feature.scanemul.params.ScannerButtonUiVerificationResult
import net.m3mobile.sdk.M3Mobile
import org.junit.Assume.assumeTrue
import org.junit.After
import org.junit.Before
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannerButtonUiInstrumentedTest {

    private var originalSettings: ScannerButtonUiSettings? = null

    @Before
    fun rememberOriginalSettings() {
        assumeTrue(
            "Scanner Button UI is unsupported on WD10",
            !Build.MODEL.contains("WD10"),
        )
        val result = awaitGet("backup-${UUID.randomUUID()}")
        assertSaved(result)
        originalSettings = checkNotNull(result.settings())
    }

    @After
    fun restoreOriginalSettings() {
        val settings = originalSettings ?: return
        awaitSet(
            ScannerButtonUiOptions(
                imagePath = settings.imagePath(),
                opacityPercent = settings.opacityPercent(),
                size = settings.size(),
            ),
            "restore-${UUID.randomUUID()}",
        )
    }

    @Test
    fun setGetAndVerifyScannerButtonUiOnSupportedDevice() {
        val fullSet = awaitSet(
            ScannerButtonUiOptions(
                imagePath = "",
                opacityPercent = 100,
                size = ScannerButtonUiSize.MEDIUM,
            ),
            "full-${UUID.randomUUID()}",
        )
        assertSaved(fullSet)

        val partialSet = awaitSet(
            ScannerButtonUiOptions.opacityPercent(20),
            "partial-${UUID.randomUUID()}",
        )
        assertSaved(partialSet)

        val get = awaitGet("get-${UUID.randomUUID()}")
        assertSaved(get)
        assertEquals(20, get.settings()?.opacityPercent())

        val verification = awaitVerify(
            ScannerButtonUiOptions(
                imagePath = "",
                opacityPercent = 100,
                size = ScannerButtonUiSize.MEDIUM,
            ),
            "verify-${UUID.randomUUID()}",
        )
        assertTrue(verification.verified())
    }

    @Test
    fun supportsAllSizesAndOpacityBoundaries() {
        ScannerButtonUiSize.values()
            .filterNot { it == ScannerButtonUiSize.UNKNOWN }
            .forEach { size ->
                val result = awaitSet(
                    ScannerButtonUiOptions.size(size),
                    "size-${size.value()}-${UUID.randomUUID()}",
                )
                assertSaved(result)
                assertEquals(size, result.settings()?.size())
            }

        listOf(20, 100).forEach { opacity ->
            val result = awaitSet(
                ScannerButtonUiOptions.opacityPercent(opacity),
                "opacity-$opacity-${UUID.randomUUID()}",
            )
            assertSaved(result)
            assertEquals(opacity, result.settings()?.opacityPercent())
        }
    }

    @Test
    fun reportsInvalidSdkRequestBeforeBroadcast() {
        val lowOpacity = awaitSet(
            ScannerButtonUiOptions(opacityPercent = 19),
            "invalid-opacity-${UUID.randomUUID()}",
        )
        assertEquals(ScannerButtonUiTransportStatus.INVALID_SDK_REQUEST, lowOpacity.transportStatus())

        val highOpacity = awaitSet(
            ScannerButtonUiOptions(opacityPercent = 101),
            "invalid-opacity-${UUID.randomUUID()}",
        )
        assertEquals(ScannerButtonUiTransportStatus.INVALID_SDK_REQUEST, highOpacity.transportStatus())

        val invalidSize = awaitSet(
            ScannerButtonUiOptions(size = ScannerButtonUiSize.UNKNOWN),
            "invalid-size-${UUID.randomUUID()}",
        )
        assertEquals(ScannerButtonUiTransportStatus.INVALID_SDK_REQUEST, invalidSize.transportStatus())
    }

    @Test
    fun reportsScanEmulInvalidImageAndUnsupportedUnknownStatusesSafely() {
        val invalidImage = awaitSet(
            ScannerButtonUiOptions.imagePath("/sdcard/Download/m3sdk_missing_button_image.png"),
            "invalid-image-${UUID.randomUUID()}",
        )
        assertEquals(ScannerButtonUiTransportStatus.OK, invalidImage.transportStatus())
        assertEquals(ScannerButtonUiStatus.INVALID_IMAGE, invalidImage.status())
    }

    @Test
    fun separatesConcurrentRequestsByRequestIdAndCompletesCallbackOnce() {
        val firstId = "same-id-${UUID.randomUUID()}"
        val first = awaitSet(ScannerButtonUiOptions.opacityPercent(20), firstId)
        val second = awaitSet(ScannerButtonUiOptions.opacityPercent(100), firstId)
        assertEquals(firstId, first.requestId())
        assertEquals(firstId, second.requestId())
        assertSaved(first)
        assertSaved(second)

        val requestCount = 4
        val latch = CountDownLatch(requestCount)
        val callbackCount = AtomicInteger(0)
        val results = mutableListOf<ScannerButtonUiResult>()
        repeat(requestCount) { index ->
            M3Mobile.instance.setScannerButtonUi(
                ScannerButtonUiOptions.opacityPercent(if (index % 2 == 0) 20 else 100),
                "concurrent-$index-${UUID.randomUUID()}",
            ) { result, error ->
                assertNull(error)
                assertNotNull(result)
                synchronized(results) {
                    results.add(checkNotNull(result))
                }
                callbackCount.incrementAndGet()
                latch.countDown()
            }
        }

        assertTrue(latch.await(15, TimeUnit.SECONDS))
        Thread.sleep(1000)
        assertEquals(requestCount, callbackCount.get())
        synchronized(results) {
            results.forEach(::assertSaved)
        }
    }

    private fun awaitSet(
        options: ScannerButtonUiOptions,
        requestId: String,
    ): ScannerButtonUiResult {
        val latch = CountDownLatch(1)
        var result: ScannerButtonUiResult? = null
        var error: Exception? = null
        M3Mobile.instance.setScannerButtonUi(options, requestId) { value, exception ->
            result = value
            error = exception
            latch.countDown()
        }
        assertTrue(latch.await(10, TimeUnit.SECONDS))
        assertNull(error)
        return checkNotNull(result)
    }

    private fun awaitGet(requestId: String): ScannerButtonUiResult {
        val latch = CountDownLatch(1)
        var result: ScannerButtonUiResult? = null
        var error: Exception? = null
        M3Mobile.instance.getScannerButtonUi(requestId) { value, exception ->
            result = value
            error = exception
            latch.countDown()
        }
        assertTrue(latch.await(10, TimeUnit.SECONDS))
        assertNull(error)
        return checkNotNull(result)
    }

    private fun awaitVerify(
        options: ScannerButtonUiOptions,
        requestId: String,
    ): ScannerButtonUiVerificationResult {
        val latch = CountDownLatch(1)
        var result: ScannerButtonUiVerificationResult? = null
        var error: Exception? = null
        M3Mobile.instance.setAndVerifyScannerButtonUi(options, requestId) { value, exception ->
            result = value
            error = exception
            latch.countDown()
        }
        assertTrue(latch.await(15, TimeUnit.SECONDS))
        assertNull(error)
        return checkNotNull(result)
    }

    private fun assertSaved(result: ScannerButtonUiResult) {
        assertEquals(result.toString(), ScannerButtonUiTransportStatus.OK, result.transportStatus())
        assertTrue(result.toString(), result.saved())
    }
}
