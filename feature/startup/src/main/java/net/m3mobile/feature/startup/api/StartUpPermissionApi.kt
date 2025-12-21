package net.m3mobile.feature.startup.api

import net.m3mobile.core.RequiresStartUp

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
}