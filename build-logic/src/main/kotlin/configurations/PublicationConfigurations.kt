package configurations

import extensions.android
import extensions.catalog
import extensions.publishing
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get

internal fun Project.configureArtifactPublication() {
    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("release") {
                    from(components["release"])

                    groupId = catalog.findVersion("groupId").get().toString()
                    artifactId = project.name
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