plugins {
    alias(libs.plugins.sdk.common)
}

android {
    namespace = "net.m3mobile.core"
}

dependencies {

    implementation(libs.kotlin.reflect)
}