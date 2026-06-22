package configurations

import extensions.android
import extensions.catalog
import extensions.publishing
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import java.net.URI

internal fun Project.configureArtifactPublication() {
    afterEvaluate {
        publishing {
            repositories {
                val githubPackagesUrl = providers.gradleProperty("m3sdk.githubPackagesUrl").orNull
                if (!githubPackagesUrl.isNullOrBlank()) {
                    maven {
                        name = "GitHubPackages"
                        url = URI(githubPackagesUrl)
                        credentials {
                            username = providers.gradleProperty("m3sdk.githubPackagesUser").orNull
                            password = providers.gradleProperty("m3sdk.githubPackagesToken").orNull
                        }
                    }
                }
            }

            publications {
                create<MavenPublication>("release") {
                    from(components["release"])

                    groupId = catalog.findVersion("groupId").get().toString()
                    artifactId = project.name
                    version = providers.gradleProperty("m3sdk.publish.version").orElse(project.version.toString()).get()
                }
            }
        }
    }

    android {
        publishing {
            singleVariant("release") {
                withSourcesJar()
                withJavadocJar()
            }
        }
    }
}