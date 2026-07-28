package net.m3mobile.feature.scanemul.params

public enum class ScannerButtonUiStatus {
    APPLIED,
    SAVED_SERVICE_NOT_READY,
    SAVED_BUTTON_NOT_VISIBLE,
    UNSUPPORTED_DEVICE,
    INVALID_REQUEST,
    INVALID_IMAGE,
    SAVE_FAILED,
    BUSY,
    UNKNOWN,
    ;

    public fun saved(): Boolean =
        this == APPLIED ||
            this == SAVED_SERVICE_NOT_READY ||
            this == SAVED_BUTTON_NOT_VISIBLE

    public companion object {
        @JvmStatic
        public fun byValue(value: String?): ScannerButtonUiStatus =
            values().firstOrNull { it.name == value } ?: UNKNOWN
    }
}
