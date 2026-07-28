package net.m3mobile.feature.scanemul.params

public enum class ScannerButtonUiSize(private val valueText: String) {
    EXTRA_SMALL("extra_small"),
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    EXTRA_LARGE("extra_large"),
    UNKNOWN("unknown"),
    ;

    public fun value(): String = valueText

    public companion object {
        @JvmStatic
        public fun byValue(value: String?): ScannerButtonUiSize =
            values().firstOrNull { it.valueText == value } ?: UNKNOWN
    }
}
