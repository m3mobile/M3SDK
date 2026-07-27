package net.m3mobile.feature.appcenter

import net.m3mobile.feature.appcenter.internal.AppCenterPasswordPolicy
import org.junit.Assert.assertThrows
import org.junit.Test

class AppCenterPasswordPolicyTest {

    @Test
    fun currentPasswordRejectsOnlyEmptyString() {
        assertThrows(IllegalArgumentException::class.java) {
            AppCenterPasswordPolicy.assertCurrentPassword("")
        }

        AppCenterPasswordPolicy.assertCurrentPassword(" ")
    }

    @Test
    fun newPasswordRejectsOutOfRangeLength() {
        assertThrows(IllegalArgumentException::class.java) {
            AppCenterPasswordPolicy.assertNewPassword(text(3))
        }
        assertThrows(IllegalArgumentException::class.java) {
            AppCenterPasswordPolicy.assertNewPassword(text(21))
        }
    }

    @Test
    fun newPasswordAcceptsBoundaryLength() {
        AppCenterPasswordPolicy.assertNewPassword(text(4))
        AppCenterPasswordPolicy.assertNewPassword(text(20))
    }

    private fun text(length: Int): String = CharArray(length) { 'p' }.concatToString()
}
