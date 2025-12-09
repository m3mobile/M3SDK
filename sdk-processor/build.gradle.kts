plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlin.poet)
    implementation(libs.androidx.annotation.jvm)
}