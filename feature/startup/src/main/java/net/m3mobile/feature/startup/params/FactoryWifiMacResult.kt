package net.m3mobile.feature.startup.params

public data class FactoryWifiMacResult public constructor(
    public val macAddress: String,
    public val success: Boolean,
    public val errorMessage: String
) {

    public val isSuccess: Boolean
        get() = success
}

internal fun factoryWifiMacResult(
    macAddress: String?,
    success: Boolean,
    errorMessage: String?
): FactoryWifiMacResult {
    return FactoryWifiMacResult(
        macAddress = macAddress ?: "",
        success = success,
        errorMessage = errorMessage ?: ""
    )
}
