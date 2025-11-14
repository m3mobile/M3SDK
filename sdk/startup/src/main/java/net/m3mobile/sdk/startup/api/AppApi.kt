package net.m3mobile.sdk.startup.api

interface AppApi {

    /**
     * Installs an APK from a local file path.
     *
     * @param filePath The absolute path to the .apk file to install
     */
    fun installApkFromLocal(filePath: String)

    /**
     * Installs an APK from a remote URL.
     *
     * @param url The URL of the APK file
     */
    fun installApkFromRemote(url: String)

    /**
     * Enables a specified application package.
     *
     * @param packageName The package name of the application to enable
     */
    fun enableApp(packageName: String)

    /**
     * Disables a specified application package.
     *
     * @param packageName The package name of the application to disable
     */
    fun disableApp(packageName: String)
}