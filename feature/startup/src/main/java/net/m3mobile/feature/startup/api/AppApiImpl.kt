package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.feature.startup.requester.app.DisableAppRequester
import net.m3mobile.feature.startup.requester.app.EnableAppRequester
import net.m3mobile.feature.startup.requester.app.InstallLocalApkRequester
import net.m3mobile.feature.startup.requester.app.InstallRemoteApkRequester

internal class AppApiImpl(private val context: Context): AppApi {

    override fun installLocalApk(filePath: String) {
        InstallLocalApkRequester(context, filePath).request()
    }

    override fun installRemoteApk(url: String) {
        InstallRemoteApkRequester(context, url).request()
    }

    override fun enableApp(packageName: String) {
        EnableAppRequester(context, packageName).request()
    }

    override fun disableApp(packageName: String) {
        DisableAppRequester(context, packageName).request()
    }
}