package net.m3mobile.feature.appcenter.internal

internal object AppCenterPasswordPolicy {

    fun assertCurrentPassword(currentPassword: String) {
        require(currentPassword.isNotEmpty()) {
            "Current AppCenter kiosk admin password is required."
        }
    }

    fun assertNewPassword(newPassword: String) {
        require(newPassword.length in MIN_NEW_PASSWORD_LENGTH..MAX_NEW_PASSWORD_LENGTH) {
            "New AppCenter kiosk admin password length must be 4 to 20 characters."
        }
    }

    private const val MIN_NEW_PASSWORD_LENGTH = 4
    private const val MAX_NEW_PASSWORD_LENGTH = 20
}
