package net.m3mobile.sdk.startup

/**
 * A singleton object that provides access to the M3Startup SDK.
 * This object serves as the main entry point for interacting with the M3 device's startup settings.
 *
 * Use [M3Startup.instance] to get a concrete implementation of the [M3StartupSdk] interface.
 */
object M3Startup {

    @JvmStatic
    val instance: M3StartupSdk = M3StartupSdkImpl
}