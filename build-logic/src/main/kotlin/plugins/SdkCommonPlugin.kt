package plugins

import configurations.configureArtifactPublication
import configurations.configureCompiler
import configurations.configureLibrary
import configurations.loadDependencies
import configurations.loadPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

class SdkCommonPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            loadPlugins()

            configureCompiler()
            configureLibrary()
            configureArtifactPublication()

            loadDependencies()
        }
    }
}