package net.m3mobile.feature.startup.requester.permission

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.ExtraValue
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

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
        ExtraKey.PACKAGE_TO_SET_PERMISSION to packageName,
        ExtraKey.PERMISSION_TO_SET to permission,
        ExtraKey.PERMISSION_STATE_TO_SET to ExtraValue.GRANT_PERMISSION
    )
}

internal class RevokePermissionRequester(
    override val context: Context,
    packageName: String,
    permission: String
): PermissionRequester() {

    override val extras = bundleOf(
        ExtraKey.PACKAGE_TO_SET_PERMISSION to packageName,
        ExtraKey.PERMISSION_TO_SET to permission,
        ExtraKey.PERMISSION_STATE_TO_SET to ExtraValue.REVOKE_PERMISSION
    )
}