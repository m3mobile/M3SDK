package net.m3mobile.m3sdk

import net.m3mobile.feature.scanemul.params.ScannerButtonUiOptions
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSettings
import net.m3mobile.feature.scanemul.params.ScannerButtonUiSize
import net.m3mobile.feature.scanemul.params.ScannerButtonUiStatus
import net.m3mobile.sdk.M3Mobile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ScannerButtonUiApiCompileTest {

    @Test
    fun scannerButtonUiModelsKeepTypedValues() {
        val options = ScannerButtonUiOptions(
            imagePath = "",
            opacityPercent = 20,
            size = ScannerButtonUiSize.EXTRA_SMALL,
        )
        assertFalse(options.empty())
        assertTrue(options.valid())

        val settings = ScannerButtonUiSettings(
            "",
            100,
            ScannerButtonUiSize.EXTRA_LARGE,
        )
        assertFalse(settings.customImage())
        assertEquals(ScannerButtonUiSize.EXTRA_LARGE, settings.size())
    }

    @Test
    fun scannerButtonUiEnumsPreserveUnknownValues() {
        assertEquals(ScannerButtonUiSize.UNKNOWN, ScannerButtonUiSize.byValue("huge"))
        assertEquals(ScannerButtonUiStatus.UNKNOWN, ScannerButtonUiStatus.byValue("NEW_STATUS"))
    }

    @Test
    fun scannerButtonUiApiIsExposedThroughM3MobileInstance() {
        // Compilation of compileOnlyReferences verifies the public facade without Android initialization.
    }

    private fun compileOnlyReferences() {
        val set: (ScannerButtonUiOptions) -> Unit = {
            M3Mobile.instance.setScannerButtonUi(it) { _, _ -> }
        }
        val setWithRequestId: (ScannerButtonUiOptions, String) -> Unit = { options, requestId ->
            M3Mobile.instance.setScannerButtonUi(options, requestId) { _, _ -> }
        }
        val get: () -> Unit = {
            M3Mobile.instance.getScannerButtonUi { _, _ -> }
        }
        val getWithRequestId: (String) -> Unit = {
            M3Mobile.instance.getScannerButtonUi(it) { _, _ -> }
        }
        val verify: (ScannerButtonUiOptions) -> Unit = {
            M3Mobile.instance.setAndVerifyScannerButtonUi(it) { _, _ -> }
        }

        @Suppress("UNUSED_VARIABLE")
        val publicMethods = listOf(set, setWithRequestId, get, getWithRequestId, verify)
    }
}
