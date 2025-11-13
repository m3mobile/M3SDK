package extensions

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

interface PublishingConfigExtension {
    val version: Property<String>
}

fun Project.publishingConfig(configure: PublishingConfigExtension.() -> Unit) {
    val extension = extensions.create("publishingConfig", PublishingConfigExtension::class.java)
    extension.configure()
}

val Project.moduleVersion: Provider<String>
    get() = extensions.getByType(PublishingConfigExtension::class.java).version