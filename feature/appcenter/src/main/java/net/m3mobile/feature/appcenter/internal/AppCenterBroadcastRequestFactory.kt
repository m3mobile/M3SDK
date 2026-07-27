package net.m3mobile.feature.appcenter.internal

internal object AppCenterBroadcastRequestFactory {

    fun passwordChangeRequest(
        currentPassword: String,
        newPassword: String,
    ): AppCenterBroadcastRequest =
        AppCenterBroadcastRequest(
            AppCenterContract.APP_PACKAGE,
            AppCenterContract.ACTION_CHANGE_PASSWORD,
            mapOf(
                AppCenterContract.EXTRA_CURRENT_PASSWORD to currentPassword,
                AppCenterContract.EXTRA_NEW_PASSWORD to newPassword,
                AppCenterContract.EXTRA_ENCRYPTION_ENABLED to true,
            )
        )

    fun keepAdminModeOnSleepRequest(enabled: Boolean): AppCenterBroadcastRequest =
        AppCenterBroadcastRequest(
            AppCenterContract.APP_PACKAGE,
            AppCenterContract.ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP,
            mapOf(
                AppCenterContract.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP to if (enabled) {
                    AppCenterContract.ENABLE_KEEP_ADMIN_MODE_ON_SLEEP
                } else {
                    AppCenterContract.DISABLE_KEEP_ADMIN_MODE_ON_SLEEP
                }
            )
        )
}
