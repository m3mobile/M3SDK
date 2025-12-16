package configurations

import com.android.build.api.dsl.LibraryExtension
import extensions.android
import extensions.catalog
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog

internal fun Project.configureLibrary() {
    android {
        configureNamespace(name)
        configureSdkVersion(this@configureLibrary.catalog)
        configureDefaults()
        includeKspResources(this@configureLibrary)
    }
}

private fun LibraryExtension.configureNamespace(moduleName: String) {
    val namespaceBase = "net.m3mobile." + if (moduleName != "core") "feature." else ""
    namespace = namespaceBase + moduleName
}

private fun LibraryExtension.configureSdkVersion(catalog: VersionCatalog) {
    compileSdk = catalog.findVersion("projectCompileSdk").get().toString().toInt()
    defaultConfig.minSdk = catalog.findVersion("projectMinSdk").get().toString().toInt()
}

private fun LibraryExtension.configureDefaults() {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
}

private fun LibraryExtension.includeKspResources(target: Project) {
    sourceSets.configureEach {
        val kspResources = target.file("build/generated/ksp/$name/resources")
        resources.srcDir(kspResources)
    }
}