package net.m3mobile.sdk.startup.requester.app

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.sdk.startup.constants.RequestAction
import net.m3mobile.sdk.startup.constants.TypeKey
import net.m3mobile.sdk.startup.constants.TypeValue

internal abstract class InstallApkRequester: BroadcastRequester() {

    override val requestAction = RequestAction.SYSTEM
    override val typeKey = TypeKey.SETTING
    override val typeValue = TypeValue.INSTALL_APK
}

internal class InstallLocalApkRequester(
    override val context: Context,
    filePath: String
): InstallApkRequester() {

    override val extras = bundleOf(
        "type" to 0,
        "path" to filePath
    )
}

internal class InstallRemoteApkRequester(
    override val context: Context,
    url: String
): InstallApkRequester() {

    override val extras = bundleOf(
        "type" to 1,
        "url" to url
    )
}