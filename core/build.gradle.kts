import extensions.publishingConfig

plugins {
    alias(libs.plugins.sdk.common)
}

android {
    namespace = "net.m3mobile.core"
    defaultConfig {
        manifestPlaceholders["M3SDK_STRICT_MODE"] = "false"
    }
}

publishingConfig {
    version.set("1.0.0")
}

dependencies {

    implementation(libs.kotlin.reflect)
}