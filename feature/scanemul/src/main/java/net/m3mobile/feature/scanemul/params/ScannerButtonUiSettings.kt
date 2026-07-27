package net.m3mobile.feature.scanemul.params

public class ScannerButtonUiSettings public constructor(
    private val imagePathValue: String,
    private val opacityPercentValue: Int,
    private val sizeValue: ScannerButtonUiSize,
) {
    public fun imagePath(): String = imagePathValue

    public fun opacityPercent(): Int = opacityPercentValue

    public fun size(): ScannerButtonUiSize = sizeValue

    public fun customImage(): Boolean = imagePathValue.isNotEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScannerButtonUiSettings) return false
        return imagePathValue == other.imagePathValue &&
            opacityPercentValue == other.opacityPercentValue &&
            sizeValue == other.sizeValue
    }

    override fun hashCode(): Int {
        var result = imagePathValue.hashCode()
        result = 31 * result + opacityPercentValue
        result = 31 * result + sizeValue.hashCode()
        return result
    }

    override fun toString(): String =
        "ScannerButtonUiSettings(" +
            "imagePath=$imagePathValue, " +
            "opacityPercent=$opacityPercentValue, " +
            "size=$sizeValue" +
            ")"
}
