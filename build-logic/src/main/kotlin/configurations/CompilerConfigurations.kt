package configurations

import extensions.kotlin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val JAVA_VERSION = 8

internal fun Project.configureJavaCompatibility() {
    kotlin {
        jvmToolchain(JAVA_VERSION)
    }
}

internal fun Project.configureFreeCompilerOptions() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.add("-opt-in=net.m3mobile.core.InternalM3Api")
        }
    }
}