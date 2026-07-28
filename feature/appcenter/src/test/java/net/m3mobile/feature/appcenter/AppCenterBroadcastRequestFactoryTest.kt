package net.m3mobile.feature.appcenter

import net.m3mobile.feature.appcenter.internal.AppCenterBroadcastRequestFactory
import net.m3mobile.feature.appcenter.internal.AppCenterContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppCenterBroadcastRequestFactoryTest {

    @Test
    fun passwordChangeRequestUsesAppCenterContract() {
        val currentPassword = text(5, 'c')
        val newPassword = text(6, 'n')

        val request = AppCenterBroadcastRequestFactory.passwordChangeRequest(
            currentPassword,
            newPassword
        )

        assertEquals(AppCenterContract.APP_PACKAGE, request.packageName)
        assertEquals(AppCenterContract.ACTION_CHANGE_PASSWORD, request.action)
        assertEquals(currentPassword, request.extra(AppCenterContract.EXTRA_CURRENT_PASSWORD))
        assertEquals(newPassword, request.extra(AppCenterContract.EXTRA_NEW_PASSWORD))
        assertEquals(true, request.extra(AppCenterContract.EXTRA_ENCRYPTION_ENABLED))
        assertFalse(request.toString().contains(currentPassword))
        assertFalse(request.toString().contains(newPassword))
    }

    @Test
    fun keepAdminModeOnSleepRequestStoresIntPolicy() {
        val enabled = AppCenterBroadcastRequestFactory.keepAdminModeOnSleepRequest(true)
        val disabled = AppCenterBroadcastRequestFactory.keepAdminModeOnSleepRequest(false)

        assertEquals(AppCenterContract.APP_PACKAGE, enabled.packageName)
        assertEquals(AppCenterContract.ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP, enabled.action)
        assertEquals(
            AppCenterContract.ENABLE_KEEP_ADMIN_MODE_ON_SLEEP,
            enabled.extra(AppCenterContract.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP)
        )
        assertEquals(
            AppCenterContract.DISABLE_KEEP_ADMIN_MODE_ON_SLEEP,
            disabled.extra(AppCenterContract.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP)
        )
        assertTrue(enabled.extra(AppCenterContract.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP) is Int)
        assertTrue(disabled.extra(AppCenterContract.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP) is Int)
    }

    @Test
    fun passwordValuesAreNotTrimmed() {
        val currentPassword = " " + text(4, 'c') + " "
        val newPassword = " " + text(4, 'n') + " "

        val request = AppCenterBroadcastRequestFactory.passwordChangeRequest(
            currentPassword,
            newPassword
        )

        assertEquals(currentPassword, request.extra(AppCenterContract.EXTRA_CURRENT_PASSWORD))
        assertEquals(newPassword, request.extra(AppCenterContract.EXTRA_NEW_PASSWORD))
    }

    private fun text(length: Int, character: Char): String =
        CharArray(length) { character }.concatToString()
}
