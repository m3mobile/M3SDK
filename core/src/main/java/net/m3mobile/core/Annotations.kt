package net.m3mobile.core

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is for internal use within the M3SDK. Do not call it directly."
)
annotation class InternalM3Api