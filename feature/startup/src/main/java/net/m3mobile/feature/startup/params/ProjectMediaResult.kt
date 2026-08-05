package net.m3mobile.feature.startup.params

public enum class ProjectMediaStatus(public val code: Int) {
    SUCCESS(0),
    UNSUPPORTED_DEVICE(1),
    TARGET_NOT_INSTALLED(2),
    INVALID_TARGET(3),
    PERMISSION_DENIED(4),
    APP_OP_UNAVAILABLE(5),
    APPLY_FAILED(6),
    ;

    internal companion object {
        fun status(code: Int): ProjectMediaStatus? =
            values().firstOrNull { it.code == code }
    }
}

public data class ProjectMediaResult public constructor(
    public val status: ProjectMediaStatus,
    public val errorMessage: String,
) {
    public val successful: Boolean
        get() = status == ProjectMediaStatus.SUCCESS
}

internal fun projectMediaResult(
    resultCode: Int,
    errorMessage: String?,
): ProjectMediaResult {
    val decodedStatus = ProjectMediaStatus.status(resultCode)
    val status = decodedStatus ?: ProjectMediaStatus.APPLY_FAILED
    val normalizedMessage = when {
        !errorMessage.isNullOrBlank() -> errorMessage
        decodedStatus == null -> "Unknown result code: $resultCode"
        else -> ""
    }
    return ProjectMediaResult(status, normalizedMessage)
}
