import extensions.publishingConfig

plugins {
    alias(libs.plugins.sdk.common)
}

android {
    namespace = "net.m3mobile.core"
}

publishingConfig {
    version.set("1.0.0")
}

dependencies {

    ksp(projects.sdkProcessor)
    
    implementation(libs.kotlin.reflect)
}