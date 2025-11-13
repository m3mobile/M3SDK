import extensions.publishingConfig

plugins {
    alias(libs.plugins.sdk.common)
}

android {
    namespace = "net.m3mobile.sdk.startup"
}

publishingConfig {
    version.set("1.0.0")
}