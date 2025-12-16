package net.m3mobile.sdk

import android.content.Context
import androidx.startup.Initializer

@Deprecated(
    message = "This is an internal class for automatic initialization and is not intended for public use.",
    level = DeprecationLevel.HIDDEN
)
public class M3SdkInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        M3Mobile.instance = M3SdkImpl(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}