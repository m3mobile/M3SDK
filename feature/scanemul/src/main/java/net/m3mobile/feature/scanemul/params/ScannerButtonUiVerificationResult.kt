package net.m3mobile.feature.scanemul.params

public class ScannerButtonUiVerificationResult public constructor(
    private val setResultValue: ScannerButtonUiResult,
    private val getResultValue: ScannerButtonUiResult?,
) {
    public fun setResult(): ScannerButtonUiResult = setResultValue

    public fun getResult(): ScannerButtonUiResult? = getResultValue

    public fun verified(): Boolean {
        val requested = setResultValue.settings() ?: return false
        val actual = getResultValue?.settings() ?: return false
        return setResultValue.saved() && getResultValue.saved() && requested == actual
    }
}
