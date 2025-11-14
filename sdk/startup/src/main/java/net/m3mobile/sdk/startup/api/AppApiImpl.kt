package net.m3mobile.sdk.startup.api

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.app.ApkInstallRequester
import net.m3mobile.sdk.startup.requester.app.DisableAppRequester
import net.m3mobile.sdk.startup.requester.app.EnableAppRequester

class AppApiImpl(private val context: Context): AppApi {

    override fun installApkFromLocal(filePath: String) {
        ApkInstallRequester(
            context = context,
            extras = bundleOf(
                "type" to 0,
                "path" to filePath
            )
        ).runBroadcast()
    }

    override fun installApkFromRemote(url: String) {
        ApkInstallRequester(
            context = context,
            extras = bundleOf(
                "type" to 1,
                "url" to url
            )
        ).runBroadcast()
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