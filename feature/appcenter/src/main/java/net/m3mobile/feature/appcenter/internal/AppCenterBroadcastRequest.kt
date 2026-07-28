package net.m3mobile.feature.appcenter.internal

internal class AppCenterBroadcastRequest internal constructor(
    val packageName: String,
    val action: String,
    private val extras: Map<String, Any>,
) {

    fun extra(key: String): Any? = extras[key]

    fun extras(): Map<String, Any> = extras.toMap()

    override fun toString(): String =
        "AppCenterBroadcastRequest(packageName=$packageName, action=$action, extras=${extras.keys})"
}
