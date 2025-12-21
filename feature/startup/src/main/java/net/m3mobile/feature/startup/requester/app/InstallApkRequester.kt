package net.m3mobile.feature.startup.requester.app

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.core.requester.BroadcastRequester
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.ExtraValue
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

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
        ExtraKey.INSTALL_APK_TYPE to ExtraValue.INSTALL_LOCAL_APK,
        ExtraKey.INSTALL_LOCAL_APK_PATH to filePath
    )
}

internal class InstallRemoteApkRequester(
    override val context: Context,
    url: String
): InstallApkRequester() {

    override val extras = bundleOf(
        ExtraKey.INSTALL_APK_TYPE to ExtraValue.INSTALL_REMOVE_APK,
        ExtraKey.INSTALL_REMOTE_APK_PATH to url
    )
}