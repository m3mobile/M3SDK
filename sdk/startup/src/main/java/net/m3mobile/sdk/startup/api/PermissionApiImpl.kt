package net.m3mobile.sdk.startup.api

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.permission.GrantPermissionRequester
import net.m3mobile.sdk.startup.requester.permission.RevokePermissionRequester

internal class PermissionApiImpl(private val context: Context): PermissionApi {

    override fun grantPermission(packageName: String, permission: String) {
        GrantPermissionRequester(context, packageName, permission).request()
    }

    override fun revokePermission(packageName: String, permission: String) {
        RevokePermissionRequester(context, packageName, permission).request()
    }
}