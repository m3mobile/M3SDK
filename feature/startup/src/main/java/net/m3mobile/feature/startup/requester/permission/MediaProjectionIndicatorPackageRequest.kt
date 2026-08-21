package net.m3mobile.feature.startup.requester.permission

import android.content.Context
import android.content.Intent
import net.m3mobile.feature.startup.constants.ExtraKey
import net.m3mobile.feature.startup.constants.RequestAction
import net.m3mobile.feature.startup.constants.TypeKey
import net.m3mobile.feature.startup.constants.TypeValue

internal enum class MediaProjectionIndicatorPackageMode(internal val value: String) {
    REPLACE("replace"),
    APPEND("append"),
    REMOVE("remove"),
    CLEAR("clear"),
}

internal class MediaProjectionIndicatorPackageRequest(
    private val context: Context,
    private val mode: MediaProjectionIndicatorPackageMode,
    private val packageNames: Collection<String>,
) {
    fun send() {
        context.sendBroadcast(intent())
    }

    private fun intent(): Intent =
        Intent(RequestAction.SYSTEM)
            .setPackage(STARTUP_PACKAGE_NAME)
            .putExtra(TypeKey.SETTING, TypeValue.MEDIA_PROJECTION_INDICATOR_PACKAGES)
            .putExtra(ExtraKey.MEDIA_PROJECTION_INDICATOR_MODE, mode.value)
            .apply {
                if (mode != MediaProjectionIndicatorPackageMode.CLEAR) {
                    putStringArrayListExtra(
                        ExtraKey.MEDIA_PROJECTION_INDICATOR_PACKAGES,
                        ArrayList(packageNames),
                    )
                }
            }

    private companion object {
        const val STARTUP_PACKAGE_NAME = "com.m3.startup"
    }
}
