package net.m3mobile.sdk.startup.api

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.app.DisableAppRequester
import net.m3mobile.sdk.startup.requester.app.EnableAppRequester
import net.m3mobile.sdk.startup.requester.app.InstallLocalApkRequester
import net.m3mobile.sdk.startup.requester.app.InstallRemoteApkRequester

class AppApiImpl(private val context: Context): AppApi {

    override fun installLocalApk(filePath: String) {
        InstallLocalApkRequester(context, filePath)
    }

    override fun installRemoteApk(url: String) {
        InstallRemoteApkRequester(context, url).runBroadcast()
    }

    override fun enableApp(packageName: String) {
        EnableAppRequester(
            context = context,
            extras = bundleOf(
                "package_name" to packageName,
                "enable" to true
            )
        ).runBroadcast()
    }

    override fun disableApp(packageName: String) {
        DisableAppRequester(
            context = context,
            extras = bundleOf(
                "package_name" to packageName,
                "enable" to false
            )
        ).runBroadcast()
    }
}