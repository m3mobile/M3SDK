package configurations

import extensions.kotlin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val JAVA_VERSION = 8

internal fun Project.configureCompiler() {
    kotlin {
        configureJavaCompatibility()
        configureFreeCompilerOptions()
        configureCompileOptions()
        explicitApi()
    }
}

private fun KotlinAndroidProjectExtension.configureJavaCompatibility() {
    jvmToolchain(JAVA_VERSION)
}

private fun KotlinAndroidProjectExtension.configureFreeCompilerOptions() {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=net.m3mobile.core.InternalM3Api")
    }
}

private fun KotlinAndroidProjectExtension.configureCompileOptions() {
    compilerOptions {
        apiVersion.set(KotlinVersion.KOTLIN_1_8)
        languageVersion.set(KotlinVersion.KOTLIN_1_8)
    }
}