package net.m3mobile.feature.scanemul.api

import java.lang.reflect.Method
import java.util.ServiceLoader
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.core.source.DeviceSupportMapSource
import net.m3mobile.core.source.MethodMapSource
import net.m3mobile.core.source.ScanEmulModelVersionMapSource
import net.m3mobile.core.source.ScanEmulVersionMapSource
import net.m3mobile.feature.generated.device.ScanEmulScannerSettingApiDeviceSupportMapSource
import net.m3mobile.feature.generated.version.ScanEmulScannerSettingApiAppVersionMapSource
import net.m3mobile.feature.generated.version.ScanEmulScannerSettingApiModelVersionMapSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScanEmulScannerButtonUiApiMetadataTest {

    @Test
    fun `generated metadata keys match every scanner button UI API overload`() {
        val inspectionKeys = ScanEmulScannerSettingApi::class.java.declaredMethods
            .filter { it.name in SCANNER_BUTTON_UI_METHOD_NAMES }
            .map(::inspectionKey)
            .toSet()

        assertEquals(SCANNER_BUTTON_UI_OVERLOAD_COUNT, inspectionKeys.size)
        assertEquals(
            inspectionKeys,
            deviceSupportSource().get<Set<String>>().scannerButtonUiKeys(),
        )
        assertEquals(
            inspectionKeys,
            appVersionSource().get<String>().scannerButtonUiKeys(),
        )
        assertEquals(
            inspectionKeys,
            modelVersionSource().get<Map<String, String>>().scannerButtonUiKeys(),
        )
    }

    @Test
    fun `scanner button UI metadata supports every known model except WD10`() {
        val expected = DeviceModel.values()
            .filterNot { it == DeviceModel.UNKNOWN || it == DeviceModel.WD10 }
            .map { it.name }
            .toSet()
        val supportedModels = deviceSupportSource()
            .get<Set<String>>()
            .scannerButtonUiValues()

        assertEquals(SCANNER_BUTTON_UI_OVERLOAD_COUNT, supportedModels.size)
        assertTrue(supportedModels.all { it == expected })
    }

    @Test
    fun `scanner button UI metadata requires ScanEmul 4_15_1 by default`() {
        val minimumVersions = appVersionSource()
            .get<String>()
            .scannerButtonUiValues()

        assertEquals(SCANNER_BUTTON_UI_OVERLOAD_COUNT, minimumVersions.size)
        assertTrue(minimumVersions.all { it == "4.15.1" })
    }

    @Test
    fun `scanner button UI metadata keeps ScanEmul 4_14_10 requirement for SM24`() {
        val modelMinimumVersions = modelVersionSource()
            .get<Map<String, String>>()
            .scannerButtonUiValues()

        assertEquals(SCANNER_BUTTON_UI_OVERLOAD_COUNT, modelMinimumVersions.size)
        assertTrue(modelMinimumVersions.all { it == mapOf("SM24" to "4.14.10") })
    }

    private fun <T> Map<String, T>.scannerButtonUiValues(): List<T> =
        filterKeys { it.scannerButtonUiKey() }.values.toList()

    private fun Map<String, *>.scannerButtonUiKeys(): Set<String> =
        keys.filter { it.scannerButtonUiKey() }.toSet()

    private fun String.scannerButtonUiKey(): Boolean =
        SCANNER_BUTTON_UI_METHOD_NAMES.any { contains(".$it(") }

    private fun inspectionKey(method: Method): String {
        val inspectionKeyMethod = Class
            .forName("net.m3mobile.core.inspection.MethodInspectorKt")
            .getDeclaredMethod("inspectionKey", Method::class.java)
            .apply { isAccessible = true }
        return inspectionKeyMethod.invoke(null, method) as String
    }

    private fun deviceSupportSource(): DeviceSupportMapSource = serviceSource(
        DeviceSupportMapSource::class.java,
        ScanEmulScannerSettingApiDeviceSupportMapSource::class.java,
    )

    private fun appVersionSource(): ScanEmulVersionMapSource = serviceSource(
        ScanEmulVersionMapSource::class.java,
        ScanEmulScannerSettingApiAppVersionMapSource::class.java,
    )

    private fun modelVersionSource(): ScanEmulModelVersionMapSource = serviceSource(
        ScanEmulModelVersionMapSource::class.java,
        ScanEmulScannerSettingApiModelVersionMapSource::class.java,
    )

    private fun <T : MethodMapSource> serviceSource(
        sourceType: Class<T>,
        expectedSourceType: Class<out T>,
    ): T = ServiceLoader.load(sourceType)
        .firstOrNull(expectedSourceType::isInstance)
        ?: error("ServiceLoader provider is missing: ${expectedSourceType.name}")

    private companion object {
        const val SCANNER_BUTTON_UI_OVERLOAD_COUNT = 12
        val SCANNER_BUTTON_UI_METHOD_NAMES = setOf(
            "setScannerButtonUi",
            "getScannerButtonUi",
            "setAndVerifyScannerButtonUi",
        )
    }
}
