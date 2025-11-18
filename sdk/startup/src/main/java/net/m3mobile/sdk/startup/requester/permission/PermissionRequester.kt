package net.m3mobile.sdk.startup.requester.permission

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class PermissionRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.PERMISSION
}

internal class GrantPermissionRequester(
    override val context: Context,
    packageName: String,
    permission: String
): PermissionRequester() {

    override val extras = bundleOf(
        "package" to packageName,
        "permission" to permission,
        "permission_mode" to 1
    )
}

internal class RevokePermissionRequester(
    override val context: Context,
    packageName: String,
    permission: String
): PermissionRequester() {

    override val extras = bundleOf(
        "package" to packageName,
        "permission" to permission,
        "permission_mode" to 2
    )
}