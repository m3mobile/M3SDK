package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.permission.GrantPermissionRequester
import net.m3mobile.feature.startup.requester.permission.RevokePermissionRequester

internal class StartUpPermissionApiImpl(private val context: Context): StartUpPermissionApi {

    override fun grantPermission(packageName: String, permission: String) {
        GrantPermissionRequester(context, packageName, permission).request()
    }

    override fun revokePermission(packageName: String, permission: String) {
        RevokePermissionRequester(context, packageName, permission).request()
    }
}