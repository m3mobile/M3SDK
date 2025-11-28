package net.m3mobile.sdk.startup.api

interface PermissionApi {

    /**
     * Grants a specific runtime permission to a target application package.
     *
     * @param packageName The unique package identifier of the application to which the permission will be granted
     * @param permission The fully qualified name of the permission to be granted
     */
    fun grantPermission(packageName: String, permission: String)

    /**
     * Revokes a specific runtime permission to a target application package.
     *
     * @param packageName The unique package identifier of the application to which the permission will be revoked
     * @param permission The fully qualified name of the permission to be revoked
     */
    fun revokePermission(packageName: String, permission: String)
}