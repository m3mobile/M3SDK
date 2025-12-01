package net.m3mobile.core

import android.content.Context
import androidx.startup.Initializer

@Deprecated(
    message = "This is an internal class for automatic initialization and is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
class M3SdkCoreInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        Configurations.run {
            this.appContext = context.applicationContext
            context.initIsStrictMode()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}