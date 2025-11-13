plugins {
    `kotlin-dsl`
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.compiler.embeddable)
}

gradlePlugin {
    plugins {
        register("sdkCommonPlugin") {
            id = "net.m3mobile.plugin.sdk.common"
            implementationClass = "plugins.SdkCommonPlugin"
        }
    }
}