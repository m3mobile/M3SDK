package net.m3mobile.feature.appcenter

import net.m3mobile.feature.appcenter.api.AppCenterKioskApiImpl
import net.m3mobile.feature.appcenter.internal.AppCenterBroadcast
import net.m3mobile.feature.appcenter.internal.AppCenterBroadcastRequest
import net.m3mobile.feature.appcenter.internal.AppCenterContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class AppCenterKioskApiImplTest {

    @Test
    fun changePasswordValidatesThenSendsRequest() {
        val availability = RecordingAvailability()
        val broadcast = RecordingBroadcast()
        val api = AppCenterKioskApiImpl(availability, broadcast)

        api.changeKioskAdminPassword(text(4, 'c'), text(4, 'n'))

        assertEquals(listOf("changeKioskAdminPassword"), availability.methodNames)
        assertEquals(1, broadcast.requests.size)
        assertEquals(
            AppCenterContract.ACTION_CHANGE_PASSWORD,
            broadcast.requests.single().action
        )
    }

    @Test
    fun invalidCurrentPasswordDoesNotSendRequest() {
        val availability = RecordingAvailability()
        val broadcast = RecordingBroadcast()
        val api = AppCenterKioskApiImpl(availability, broadcast)

        assertThrows(IllegalArgumentException::class.java) {
            api.changeKioskAdminPassword("", text(4, 'n'))
        }

        assertTrue(availability.methodNames.isEmpty())
        assertTrue(broadcast.requests.isEmpty())
    }

    @Test
    fun invalidNewPasswordDoesNotSendRequest() {
        val availability = RecordingAvailability()
        val broadcast = RecordingBroadcast()
        val api = AppCenterKioskApiImpl(availability, broadcast)

        assertThrows(IllegalArgumentException::class.java) {
            api.changeKioskAdminPassword(text(4, 'c'), text(3, 'n'))
        }

        assertTrue(availability.methodNames.isEmpty())
        assertTrue(broadcast.requests.isEmpty())
    }

    @Test
    fun keepAdminModeOnSleepSendsRequest() {
        val availability = RecordingAvailability()
        val broadcast = RecordingBroadcast()
        val api = AppCenterKioskApiImpl(availability, broadcast)

        api.setKeepAdminModeOnSleep(true)

        assertEquals(listOf("setKeepAdminModeOnSleep"), availability.methodNames)
        assertEquals(1, broadcast.requests.size)
        assertEquals(
            AppCenterContract.ENABLE_KEEP_ADMIN_MODE_ON_SLEEP,
            broadcast.requests.single().extra(AppCenterContract.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP)
        )
    }

    private class RecordingAvailability : AppCenterAvailability {
        val methodNames = mutableListOf<String>()

        override fun assertAppAvailable(methodName: String) {
            methodNames += methodName
        }
    }

    private class RecordingBroadcast : AppCenterBroadcast {
        val requests = mutableListOf<AppCenterBroadcastRequest>()

        override fun send(request: AppCenterBroadcastRequest) {
            requests += request
        }
    }

    private fun text(length: Int, character: Char): String =
        CharArray(length) { character }.concatToString()
}
