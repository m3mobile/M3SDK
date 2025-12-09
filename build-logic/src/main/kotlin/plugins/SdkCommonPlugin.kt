package plugins

import configurations.configureArtifactPublication
import configurations.configureFreeCompilerOptions
import configurations.configureJavaCompatibility
import configurations.configureLibrary
import configurations.loadDependencies
import configurations.loadPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class SdkCommonPlugin: Plugin<Project> {

    /**
     * Library 배포에서 제외할 모듈
     */
    private val excludePublishModules = setOf("keytool", "scanemul")

    override fun apply(target: Project) {
        target.run {
            loadPlugins()

            configureJavaCompatibility()
            configureFreeCompilerOptions()
            configureLibrary()
            if (project.name !in excludePublishModules)
                configureArtifactPublication()

            loadDependencies()
        }
    }
}