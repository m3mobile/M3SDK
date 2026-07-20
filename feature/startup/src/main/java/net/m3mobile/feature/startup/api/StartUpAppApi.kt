package net.m3mobile.feature.startup.api

import net.m3mobile.core.RequiresStartUp

public interface StartUpAppApi {

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
     * Installs an APK from a local file path and optionally permits reinstalling the same version code.
     *
     * StartUp version `6.8.1` or later is required.
     *
     * @param filePath The absolute path to the .apk file to install
     * @param allowSameVersionUpdate Whether to reinstall when the APK version code matches the installed app
     */
    @RequiresStartUp("6.8.1")
    public fun installLocalApk(filePath: String, allowSameVersionUpdate: Boolean)

    /**
     * Installs an APK from a local file path with same-version and post-install launch options.
     *
     * StartUp version `6.8.2` or later is required. The request is one-way; a normal return confirms
     * only that the broadcast was sent, not that installation or launch succeeded.
     *
     * @param filePath The absolute path to the .apk file to install
     * @param allowSameVersionUpdate Whether to reinstall when the APK version code matches the installed app
     * @param launchAfterInstall Whether to launch the installed app after installation succeeds
     */
    @RequiresStartUp("6.8.2")
    public fun installLocalApk(
        filePath: String,
        allowSameVersionUpdate: Boolean,
        launchAfterInstall: Boolean
    )

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
     * Installs an APK from a remote URL and optionally permits reinstalling the same version code.
     *
     * StartUp version `6.8.1` or later is required.
     *
     * @param url The URL of the APK file
     * @param allowSameVersionUpdate Whether to reinstall when the APK version code matches the installed app
     */
    @RequiresStartUp("6.8.1")
    public fun installRemoteApk(url: String, allowSameVersionUpdate: Boolean)

    /**
     * Installs an APK from a remote URL with same-version and post-install launch options.
     *
     * StartUp version `6.8.2` or later is required. The request is one-way; a normal return confirms
     * only that the broadcast was sent, not that download, installation, or launch succeeded.
     *
     * @param url The URL of the APK file
     * @param allowSameVersionUpdate Whether to reinstall when the APK version code matches the installed app
     * @param launchAfterInstall Whether to launch the installed app after installation succeeds
     */
    @RequiresStartUp("6.8.2")
    public fun installRemoteApk(
        url: String,
        allowSameVersionUpdate: Boolean,
        launchAfterInstall: Boolean
    )

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

    /**
     * Enables and runs a specified application package.
     *
     * StartUp version `6.8.0` or later is required.
     *
     * @param packageName The package name of the application to enable and run
     */
    @RequiresStartUp("6.8.0")
    public fun runApp(packageName: String)

    /**
     * Enables, runs, and pins a specified application package.
     *
     * StartUp version `6.8.0` or later is required.
     *
     * @param packageName The package name of the application to enable, run, and pin
     */
    @RequiresStartUp("6.8.0")
    public fun runAndPinApp(packageName: String)
}
