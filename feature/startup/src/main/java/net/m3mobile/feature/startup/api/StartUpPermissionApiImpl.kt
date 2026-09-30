package net.m3mobile.feature.startup.api

import android.content.Context
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.utils.launchOnMain
import net.m3mobile.feature.startup.params.ProjectMediaResult
import net.m3mobile.feature.startup.requester.permission.MediaProjectionIndicatorPackageMode
import net.m3mobile.feature.startup.requester.permission.MediaProjectionIndicatorPackageRequest
import net.m3mobile.feature.startup.requester.permission.GrantPermissionRequester
import net.m3mobile.feature.startup.requester.permission.ProjectMediaRequest
import net.m3mobile.feature.startup.requester.permission.RevokePermissionRequester

internal class StartUpPermissionApiImpl(private val context: Context): StartUpPermissionApi {

    override fun grantPermission(packageName: String, permission: String) {
        GrantPermissionRequester(context, packageName, permission).request()
    }

    override fun revokePermission(packageName: String, permission: String) {
        RevokePermissionRequester(context, packageName, permission).request()
    }

    override suspend fun allowProjectMedia(packageName: String): ProjectMediaResult =
        ProjectMediaRequest(context, packageName).result()

    override fun allowProjectMedia(
        packageName: String,
        callback: RequestCallback<ProjectMediaResult>,
    ) = launchOnMain {
        try {
            callback.onComplete(allowProjectMedia(packageName), null)
        } catch (e: Exception) {
            callback.onComplete(null, e)
        }
    }

    override fun setMediaProjectionIndicatorExemptPackages(vararg packageNames: String) {
        sendMediaProjectionIndicatorPackageRequest(
            MediaProjectionIndicatorPackageMode.REPLACE,
            packageNames,
        )
    }

    override fun addMediaProjectionIndicatorExemptPackages(vararg packageNames: String) {
        sendMediaProjectionIndicatorPackageRequest(
            MediaProjectionIndicatorPackageMode.APPEND,
            packageNames,
        )
    }

    override fun removeMediaProjectionIndicatorExemptPackages(vararg packageNames: String) {
        sendMediaProjectionIndicatorPackageRequest(
            MediaProjectionIndicatorPackageMode.REMOVE,
            packageNames,
        )
    }

    override fun clearMediaProjectionIndicatorExemptPackages() {
        sendMediaProjectionIndicatorPackageRequest(
            MediaProjectionIndicatorPackageMode.CLEAR,
            emptyArray(),
        )
    }

    private fun sendMediaProjectionIndicatorPackageRequest(
        mode: MediaProjectionIndicatorPackageMode,
        packageNames: Array<out String>,
    ) {
        MediaProjectionIndicatorPackageRequest(context, mode, packageNames.asList()).send()
    }
}
