package net.m3mobile.feature.startup.api

import net.m3mobile.feature.generated.version.StartUpPermissionApiAppVersionMapSource
import net.m3mobile.feature.generated.device.StartUpPermissionApiDeviceSupportMapSource
import org.junit.Assert.assertEquals
import org.junit.Test

class StartUpPermissionApiMetadataTest {
    @Test
    fun `all PROJECT_MEDIA overloads require StartUp 6_8_4`() {
        val versionMap = StartUpPermissionApiAppVersionMapSource().get<String>()

        assertEquals(
            setOf("6.8.4"),
            versionMap.filterKeys { it.contains(".allowProjectMedia(") }.values.toSet(),
        )
    }

    @Test
    fun `all MediaProjection indicator package APIs require StartUp 6_8_7`() {
        val versionMap = StartUpPermissionApiAppVersionMapSource().get<String>()

        assertEquals(
            setOf("6.8.7"),
            versionMap.filterKeys { it.contains("MediaProjectionIndicatorExemptPackages(") }
                .values
                .toSet(),
        )
    }

    @Test
    fun `all MediaProjection indicator package APIs support SM24 only`() {
        val supportMap = StartUpPermissionApiDeviceSupportMapSource().get<Set<String>>()

        assertEquals(
            setOf(setOf("SM24")),
            supportMap.filterKeys { it.contains("MediaProjectionIndicatorExemptPackages(") }
                .values
                .toSet(),
        )
    }
}
