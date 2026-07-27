package net.m3mobile.feature.scanemul.params

public class ScannerButtonUiResult public constructor(
    private val requestIdValue: String,
    private val transportStatusValue: ScannerButtonUiTransportStatus,
    private val successValue: Boolean,
    private val statusValue: ScannerButtonUiStatus,
    private val rawStatusValue: String?,
    private val runtimeAppliedValue: Boolean,
    private val settingsValue: ScannerButtonUiSettings?,
) {
    public fun requestId(): String = requestIdValue

    public fun transportStatus(): ScannerButtonUiTransportStatus = transportStatusValue

    public fun success(): Boolean = successValue

    public fun status(): ScannerButtonUiStatus = statusValue

    public fun rawStatus(): String? = rawStatusValue

    public fun runtimeApplied(): Boolean = runtimeAppliedValue

    public fun settings(): ScannerButtonUiSettings? = settingsValue

    public fun saved(): Boolean =
        transportStatusValue == ScannerButtonUiTransportStatus.OK &&
            successValue &&
            statusValue.saved()

    override fun toString(): String =
        "ScannerButtonUiResult(" +
            "requestId=$requestIdValue, " +
            "transportStatus=$transportStatusValue, " +
            "success=$successValue, " +
            "status=$statusValue, " +
            "rawStatus=$rawStatusValue, " +
            "runtimeApplied=$runtimeAppliedValue, " +
            "settings=$settingsValue" +
            ")"
}
