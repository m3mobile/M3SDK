package net.m3mobile.feature.scanemul.params

public data class ScanResult(
    val barcode: String,
    val type: String,
    val rawData: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScanResult

        if (barcode != other.barcode) return false
        if (type != other.type) return false
        if (!rawData.contentEquals(other.rawData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = barcode.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + rawData.contentHashCode()
        return result
    }
}