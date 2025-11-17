package net.m3mobile.sdk.startup.api

interface PermissionApi {

    fun grantPermission(packageName: String, permission: String)

    fun revokePermission(packageName: String, permission: String)
}