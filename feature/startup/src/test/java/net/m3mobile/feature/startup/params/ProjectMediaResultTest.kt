package net.m3mobile.feature.startup.params

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProjectMediaResultTest {
    @Test
    fun `success code creates successful result`() {
        val result = projectMediaResult(0, null)

        assertEquals(ProjectMediaStatus.SUCCESS, result.status)
        assertTrue(result.successful)
        assertEquals("", result.errorMessage)
    }

    @Test
    fun `known failure codes preserve status and message`() {
        val expectedStatuses = ProjectMediaStatus.values()
            .filterNot { it == ProjectMediaStatus.SUCCESS }

        expectedStatuses.forEach { expectedStatus ->
            val result = projectMediaResult(expectedStatus.code, "failure")

            assertEquals(expectedStatus, result.status)
            assertFalse(result.successful)
            assertEquals("failure", result.errorMessage)
        }
    }

    @Test
    fun `unknown code becomes apply failed with diagnostic message`() {
        val result = projectMediaResult(99, null)

        assertEquals(ProjectMediaStatus.APPLY_FAILED, result.status)
        assertFalse(result.successful)
        assertEquals("Unknown result code: 99", result.errorMessage)
    }

    @Test
    fun `public status contract matches StartUp codes`() {
        assertEquals(
            listOf(0, 1, 2, 3, 4, 5, 6),
            ProjectMediaStatus.values().map(ProjectMediaStatus::code),
        )
    }
}
