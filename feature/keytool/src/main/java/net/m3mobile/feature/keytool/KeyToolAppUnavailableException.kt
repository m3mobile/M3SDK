package net.m3mobile.feature.keytool

/**
 * Thrown when a KeyTool API cannot reach the companion app required by that API.
 */
public class KeyToolAppUnavailableException(message: String) : RuntimeException(message) {

    internal constructor(methodName: String, appName: String, packageName: String) : this(
        "\"$methodName\" is unavailable because $appName ($packageName) is not installed " +
            "or is not visible to the SDK."
    )
}
