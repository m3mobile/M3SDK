package net.m3mobile.sdk.startup.api

import android.content.Context
import net.m3mobile.sdk.startup.requester.app.DisableAppRequester
import net.m3mobile.sdk.startup.requester.app.EnableAppRequester
import net.m3mobile.sdk.startup.requester.app.InstallLocalApkRequester
import net.m3mobile.sdk.startup.requester.app.InstallRemoteApkRequester

class AppApiImpl(private val context: Context): AppApi {

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