package net.m3mobile.feature.scanemul.params

import androidx.annotation.IntRange

public class ScannerButtonUiOptions @JvmOverloads public constructor(
    private val imagePath: String? = null,
    @IntRange(from = 20, to = 100)
    private val opacityPercent: Int? = null,
    private val size: ScannerButtonUiSize? = null,
) {
    public fun imagePath(): String? = imagePath

    public fun opacityPercent(): Int? = opacityPercent

    public fun size(): ScannerButtonUiSize? = size

    public fun empty(): Boolean =
        imagePath == null && opacityPercent == null && size == null

    public fun valid(): Boolean =
        !empty() &&
            opacityPercent?.let {
                it in MIN_OPACITY_PERCENT..MAX_OPACITY_PERCENT
            } != false &&
            size != ScannerButtonUiSize.UNKNOWN

    public companion object {
        public const val MIN_OPACITY_PERCENT: Int = 20
        public const val MAX_OPACITY_PERCENT: Int = 100

        @JvmStatic
        public fun imagePath(imagePath: String): ScannerButtonUiOptions =
            ScannerButtonUiOptions(imagePath = imagePath)

        @JvmStatic
        public fun opacityPercent(
            @IntRange(from = 20, to = 100)
            opacityPercent: Int,
        ): ScannerButtonUiOptions =
            ScannerButtonUiOptions(opacityPercent = opacityPercent)

        @JvmStatic
        public fun size(size: ScannerButtonUiSize): ScannerButtonUiOptions =
            ScannerButtonUiOptions(size = size)
    }
}
