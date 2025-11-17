package net.m3mobile.sdk.startup.api

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.permission.GrantPermissionRequester
import net.m3mobile.sdk.startup.requester.permission.RevokePermissionRequester

class PermissionApiImpl(private val context: Context): PermissionApi {

    override fun grantPermission(packageName: String, permission: String) {
        GrantPermissionRequester(context, bundleOf(
            "package" to packageName,
            "permission" to permission,
            "permission_mode" to 1
        )).runBroadcast()
    }

    override fun revokePermission(packageName: String, permission: String) {
        RevokePermissionRequester(context, bundleOf(
            "package" to packageName,
            "permission" to permission,
            "permission_mode" to 2
        )).runBroadcast()
    }
}