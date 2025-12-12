package net.m3mobile.sdk.keytool

import android.content.Context
import androidx.startup.Initializer

@Deprecated(
    message = "This is an internal class for automatic initialization and is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
public class M3KeyToolSdkInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        M3KeyTool.init(context.applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}