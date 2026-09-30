package net.m3mobile.samples.compose

import androidx.annotation.StringRes

internal enum class SampleCategory(@StringRes val titleRes: Int) {
    DEVICE_INFO(R.string.device_info),
    AIRPLANE_MODE(R.string.airplane_mode),
    APPLICATION(R.string.application),
    DEVICE(R.string.device),
    LANGUAGE(R.string.language),
    NETWORK(R.string.network),
    PERMISSION(R.string.permission),
    PROJECT_MEDIA(R.string.project_media),
    MEDIA_PROJECTION_INDICATOR(R.string.media_projection_indicator),
    QUICK_TILE(R.string.quick_tile),
    SCANNER(R.string.scanner),
    STARTUP_SETTING(R.string.startup_setting),
    TIME(R.string.time),
    USB(R.string.usb),
    WIFI(R.string.wifi),
    APPCENTER(R.string.appcenter),
    KEYTOOL(R.string.keytool)
}
