package configurations

import org.gradle.api.Project

internal fun Project.loadPlugins() {
    pluginManager.run {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("maven-publish")
        if (project.name != "core")
            apply("com.google.devtools.ksp")
    }
}