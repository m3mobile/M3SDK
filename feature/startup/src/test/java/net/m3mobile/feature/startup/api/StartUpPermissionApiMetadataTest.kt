package net.m3mobile.feature.startup.api

import net.m3mobile.feature.generated.version.StartUpPermissionApiAppVersionMapSource
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
}
