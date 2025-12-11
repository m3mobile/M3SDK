package configurations

import extensions.api
import extensions.catalog
import extensions.implementation
import extensions.ksp
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal fun Project.loadDependencies() {
    dependencies {
        if (project.name != "core") {
            api(project(":core"))
            ksp(project(":sdk-processor"))
        }

        implementation(catalog.findLibrary("startup-runtime").get())
        implementation(catalog.findLibrary("androidx-core-ktx").get())
        implementation(catalog.findLibrary("kotlinx-coroutines-android").get())
    }
}