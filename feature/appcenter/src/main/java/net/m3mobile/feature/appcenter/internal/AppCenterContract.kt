package net.m3mobile.feature.appcenter.internal

internal object AppCenterContract {
    const val APP_NAME = "AppCenter"
    const val APP_PACKAGE = "com.m3.appcenter"
    const val REQUIRED_VERSION = "2.2.0"

    const val ACTION_CHANGE_PASSWORD = "com.m3.appcenter.ACTION_CHANGE_PASSWORD"
    const val ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP =
        "com.m3.appcenter.ACTION_SET_KEEP_ADMIN_MODE_ON_SLEEP"

    const val EXTRA_CURRENT_PASSWORD = "com.m3.appcenter.EXTRA_CURRENT_PASSWORD"
    const val EXTRA_NEW_PASSWORD = "com.m3.appcenter.EXTRA_NEW_PASSWORD"
    const val EXTRA_ENCRYPTION_ENABLED = "com.m3.appcenter.EXTRA_ENCRYPTION_ENABLED"
    const val EXTRA_KEEP_ADMIN_MODE_ON_SLEEP =
        "com.m3.appcenter.EXTRA_KEEP_ADMIN_MODE_ON_SLEEP"

    const val ENABLE_KEEP_ADMIN_MODE_ON_SLEEP = 1
    const val DISABLE_KEEP_ADMIN_MODE_ON_SLEEP = 0
}
