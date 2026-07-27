package net.m3mobile.feature.appcenter.api

import android.content.Context
import net.m3mobile.feature.appcenter.AppCenterAppGuard
import net.m3mobile.feature.appcenter.AppCenterAvailability
import net.m3mobile.feature.appcenter.internal.AppCenterBroadcast
import net.m3mobile.feature.appcenter.internal.AppCenterBroadcastRequestFactory
import net.m3mobile.feature.appcenter.internal.AppCenterPasswordPolicy
import net.m3mobile.feature.appcenter.internal.ExplicitAppCenterBroadcast

internal class AppCenterKioskApiImpl(
    private val appCenterAvailability: AppCenterAvailability,
    private val appCenterBroadcast: AppCenterBroadcast,
) : AppCenterKioskApi {

    constructor(context: Context) : this(
        AppCenterAppGuard(context),
        ExplicitAppCenterBroadcast(context)
    )

    override fun changeKioskAdminPassword(currentPassword: String, newPassword: String) {
        AppCenterPasswordPolicy.assertCurrentPassword(currentPassword)
        AppCenterPasswordPolicy.assertNewPassword(newPassword)
        appCenterAvailability.assertAppAvailable("changeKioskAdminPassword")
        appCenterBroadcast.send(
            AppCenterBroadcastRequestFactory.passwordChangeRequest(currentPassword, newPassword)
        )
    }

    override fun setKeepAdminModeOnSleep(enabled: Boolean) {
        appCenterAvailability.assertAppAvailable("setKeepAdminModeOnSleep")
        appCenterBroadcast.send(
            AppCenterBroadcastRequestFactory.keepAdminModeOnSleepRequest(enabled)
        )
    }
}
