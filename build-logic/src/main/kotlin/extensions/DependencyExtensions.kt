package extensions

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandler.compileOnly(dependency: Any) {
    add("compileOnly", dependency)
}

fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}

fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}