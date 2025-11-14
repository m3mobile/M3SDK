package net.m3mobile.sdk.startup.api

import android.content.Context
import androidx.core.os.bundleOf
import net.m3mobile.sdk.startup.requester.app.ApkInstallRequester

class AppApiImpl(private val context: Context): AppApi {

    override fun installApkFromLocal(filePath: String) {
        ApkInstallRequester(
            context = context,
            extras = bundleOf(
                "type" to 0,
                "path" to filePath
            )
        )
    }

    override fun installApkFromRemote(url: String) {
        ApkInstallRequester(
            context = context,
            extras = bundleOf(
                "type" to 1,
                "url" to url
            )
        )
    }
}