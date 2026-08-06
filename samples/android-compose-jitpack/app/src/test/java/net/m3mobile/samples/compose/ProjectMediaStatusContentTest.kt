package net.m3mobile.samples.compose

import net.m3mobile.feature.startup.params.ProjectMediaStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectMediaStatusContentTest {
    @Test
    fun `every SDK status has condition and action content`() {
        val contents = ProjectMediaStatus.entries.associateWith(ProjectMediaStatus::content)

        assertEquals(ProjectMediaStatus.entries.toSet(), contents.keys)
        contents.values.forEach { content ->
            assertTrue(content.conditionRes != 0)
            assertTrue(content.actionRes != 0)
        }
    }
}
