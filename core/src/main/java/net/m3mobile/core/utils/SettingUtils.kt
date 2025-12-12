package net.m3mobile.core.utils

import android.content.Context
import android.provider.Settings
import net.m3mobile.core.InternalM3Api

@InternalM3Api
public fun Context.getGlobalInt(name: String, defaultValue: Int): Int {
    return try {
        Settings.Global.getInt(contentResolver, name)
    } catch (_: Exception) {
        defaultValue
    }
}

@InternalM3Api
public fun Context.getGlobalString(name: String, defaultValue: String = ""): String {
    return Settings.Global.getString(contentResolver, name) ?: defaultValue
}