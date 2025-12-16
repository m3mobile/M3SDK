package net.m3mobile.feature.startup.api

import net.m3mobile.core.RequiresStartUp

public interface AppApi {

    /**
     * Installs an APK from a local file path.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param filePath The absolute path to the .apk file to install
     */
    @RequiresStartUp("6.2.14")
    public fun installLocalApk(filePath: String)

    /**
     * Installs an APK from a remote URL.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param url The URL of the APK file
     */
    @RequiresStartUp("6.2.14")
    public fun installRemoteApk(url: String)

    /**
     * Enables a specified application package.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param packageName The package name of the application to enable
     */
    @RequiresStartUp("6.2.14")
    public fun enableApp(packageName: String)

    /**
     * Disables a specified application package.
     *
     * StartUp version `6.2.14` or later is required.
     *
     * @param packageName The package name of the application to disable
     */
    @RequiresStartUp("6.2.14")
    public fun disableApp(packageName: String)
}