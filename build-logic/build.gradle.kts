plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.kotlin.compiler.embeddable)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("sdkCommonPlugin") {
            id = "net.m3mobile.plugin.sdk.common"
            implementationClass = "plugins.SdkCommonPlugin"
        }
    }
}