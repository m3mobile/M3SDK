package net.m3mobile.feature.startup.api

import kotlinx.coroutines.Job
import net.m3mobile.core.RequestCallback
import net.m3mobile.core.RequiresStartUp
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
}
