package net.m3mobile.feature.startup.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.RequiresStartUp
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.feature.startup.params.ProjectMediaResult

public interface StartUpPermissionApi {

    /**
     * Grants a specific runtime permission to a target application package.
     *
     * StartUp version `6.4.17` or later is required.
     *
     *  @param packageName The unique package identifier of the application to which the permission will be granted
     * @param permission The fully qualified name of the permission to be granted
     */
    @RequiresStartUp("6.4.17")
    public fun grantPermission(packageName: String, permission: String)

    /**
     * Revokes a specific runtime permission to a target application package.
     *
     * StartUp version `6.4.17` or later is required.
     *
     * @param packageName The unique package identifier of the application to which the permission will be revoked
     * @param permission The fully qualified name of the permission to be revoked
     */
    @RequiresStartUp("6.4.17")
    public fun revokePermission(packageName: String, permission: String)

    /**
     * Allows the Android `PROJECT_MEDIA` app operation for an installed package.
     *
     * StartUp version `6.8.4` or later is required. This API currently supports SM24 only.
     *
     * @param packageName The installed package that will receive `PROJECT_MEDIA`.
     * The returned [ProjectMediaResult] contains one of the seven StartUp status codes. Transport
     * failures such as a missing response are thrown separately.
     */
    @RequiresStartUp("6.8.4")
    public suspend fun allowProjectMedia(packageName: String): ProjectMediaResult

    /**
     * Allows the Android `PROJECT_MEDIA` app operation for an installed package.
     *
     * StartUp version `6.8.4` or later is required. The callback receives all StartUp feature
     * outcomes as [ProjectMediaResult]; only transport failures are delivered as callback errors.
     *
     * @param packageName The installed package that will receive `PROJECT_MEDIA`.
     * @param callback A callback that receives the StartUp result or a communication error.
     * @return A job that can cancel the pending callback request.
     */
    @RequiresStartUp("6.8.4")
    public fun allowProjectMedia(
        packageName: String,
        callback: RequestCallback<ProjectMediaResult>,
    ): Job

    /**
     * Replaces the packages for which SM24 hides the MediaProjection screen-recording indicator.
     *
     * StartUp version `6.8.7` or later is required. This is a one-way request. StartUp persists the
     * normalized package list and restores it after app or device restart. Changes apply to
     * MediaProjection sessions started after this request; restart an active session to apply them.
     *
     * @param packageNames Package names that replace the complete exception list. Passing no names
     * clears the list.
     */
    @SupportedModels(DeviceModel.SM24)
    @RequiresStartUp("6.8.7")
    public fun setMediaProjectionIndicatorExemptPackages(vararg packageNames: String)

    /**
     * Adds packages for which SM24 hides the MediaProjection screen-recording indicator.
     *
     * StartUp version `6.8.7` or later is required. Blank and duplicate names are normalized by
     * StartUp. This is a one-way request. Changes apply to MediaProjection sessions started after
     * this request; restart an active session to apply them.
     */
    @SupportedModels(DeviceModel.SM24)
    @RequiresStartUp("6.8.7")
    public fun addMediaProjectionIndicatorExemptPackages(vararg packageNames: String)

    /**
     * Removes packages from the SM24 MediaProjection screen-recording indicator exception list.
     *
     * StartUp version `6.8.7` or later is required. This is a one-way request. Changes apply to
     * MediaProjection sessions started after this request; restart an active session to apply them.
     */
    @SupportedModels(DeviceModel.SM24)
    @RequiresStartUp("6.8.7")
    public fun removeMediaProjectionIndicatorExemptPackages(vararg packageNames: String)

    /**
     * Clears all SM24 MediaProjection screen-recording indicator exceptions.
     *
     * StartUp version `6.8.7` or later is required. This is a one-way request. Changes apply to
     * MediaProjection sessions started after this request; restart an active session to apply them.
     */
    @SupportedModels(DeviceModel.SM24)
    @RequiresStartUp("6.8.7")
    public fun clearMediaProjectionIndicatorExemptPackages()
}
