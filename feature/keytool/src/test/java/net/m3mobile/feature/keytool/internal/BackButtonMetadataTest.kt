package net.m3mobile.feature.keytool.internal

import java.util.ServiceLoader
import net.m3mobile.core.source.DeviceSupportMapSource
import org.junit.Assert.assertEquals
import org.junit.Test

class BackButtonMetadataTest {
    @Test
    fun generatedBackSupportIsSm24OnlyAndHomeRecentRemainShared() {
        val source = ServiceLoader.load(DeviceSupportMapSource::class.java)
            .single { it.javaClass.simpleName == "KeyToolKeyApiDeviceSupportMapSource" }
        val metadata = source.get<Set<String>>()
        val back = metadata.filterKeys { key ->
            key.contains(".enableBackButton(") || key.contains(".disableBackButton(")
        }
        val existing = metadata.filterKeys { key ->
            listOf("enableHomeButton", "disableHomeButton", "enableRecentButton", "disableRecentButton")
                .any { key.contains(".$it(") }
        }
        assertEquals(2, back.size)
        back.values.forEach { assertEquals(setOf("SM24"), it) }
        assertEquals(4, existing.size)
        existing.values.forEach { assertEquals(setOf("SM24", "SM25"), it) }
    }
}
