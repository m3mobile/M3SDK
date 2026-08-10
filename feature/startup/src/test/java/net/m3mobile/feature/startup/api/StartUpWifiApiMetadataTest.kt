package net.m3mobile.feature.startup.api

import net.m3mobile.feature.generated.device.StartUpWifiApiDeviceSupportMapSource
import net.m3mobile.feature.generated.version.StartUpWifiApiAppVersionMapSource
import net.m3mobile.feature.generated.version.StartUpWifiApiModelVersionMapSource
import org.junit.Assert.assertEquals
import org.junit.Test

class StartUpWifiApiMetadataTest {

    @Test
    fun `setWifiEnabled supports all StartUp wifi state change models`() {
        val supportMap = StartUpWifiApiDeviceSupportMapSource().get<Set<String>>()

        assertEquals(
            setOf(
                "SM20",
                "SM20_U",
                "SL20",
                "SL20P",
                "SL20K",
                "US20",
                "US30",
                "UL20_OREO",
                "UL20_PIE",
                "UX20_Q",
                "UL20_A10",
                "UL20F",
                "UL30",
                "SM24",
                "SM25",
                "PC10",
                "WD10",
            ),
            supportMap.wifiEnabledValue(),
        )
    }

    @Test
    fun `setWifiEnabled requires StartUp 6_8_5 by default`() {
        val versionMap = StartUpWifiApiAppVersionMapSource().get<String>()

        assertEquals("6.8.5", versionMap.wifiEnabledValue())
    }

    @Test
    fun `setWifiEnabled keeps StartUp 6_8_3 requirement for SM24`() {
        val modelVersionMap =
            StartUpWifiApiModelVersionMapSource().get<Map<String, String>>()

        assertEquals(mapOf("SM24" to "6.8.3"), modelVersionMap.wifiEnabledValue())
    }

    private fun <T> Map<String, T>.wifiEnabledValue(): T =
        entries.single { (key, _) -> key.contains(".setWifiEnabled(") }.value
}
