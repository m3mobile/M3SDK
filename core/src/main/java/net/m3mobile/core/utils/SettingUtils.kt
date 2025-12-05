package net.m3mobile.core.utils

import android.content.Context
import android.provider.Settings

fun Context.getGlobalInt(name: String, defaultValue: Int): Int {
    return try {
        Settings.Global.getInt(contentResolver, name)
    } catch (_: Exception) {
        defaultValue
    }
}

fun Context.getGlobalString(name: String, defaultValue: String = ""): String {
    return Settings.Global.getString(contentResolver, name) ?: defaultValue
}