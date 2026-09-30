package net.m3mobile.feature.startup.requester.permission

import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.TypeValue
import org.junit.Assert.assertEquals
import org.junit.Test

class MediaProjectionIndicatorPackageRequestTest {
    @Test
    fun `request contract matches StartUp 6_8_7`() {
        assertEquals(
            "media_projection_exempt_packages",
            TypeValue.MEDIA_PROJECTION_INDICATOR_PACKAGES,
        )
        assertEquals("packages", ExtraKey.MEDIA_PROJECTION_INDICATOR_PACKAGES)
        assertEquals("mode", ExtraKey.MEDIA_PROJECTION_INDICATOR_MODE)
        assertEquals(
            listOf("replace", "append", "remove", "clear"),
            MediaProjectionIndicatorPackageMode.values().map { it.value },
        )
    }
}
