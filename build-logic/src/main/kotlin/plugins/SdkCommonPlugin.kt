package plugins

import com.android.build.api.dsl.LibraryExtension
import extensions.api
import extensions.catalog
import extensions.configureKotlinAndroid
import extensions.implementation
import extensions.moduleVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class SdkCommonPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("maven-publish")
                apply("com.google.devtools.ksp")
            }

            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    freeCompilerArgs.add("-opt-in=net.m3mobile.core.InternalM3Api")
                }
            }

            afterEvaluate {
                extensions.configure<PublishingExtension> {
                    publications {
                        create<MavenPublication>("release") {
                            from(components["release"])

                            groupId = "net.m3mobile.sdk"
                            artifactId = project.name
                            version = moduleVersion.get()
                        }
                    }
                }
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    release {
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                publishing {
                    singleVariant("release") {
                        withSourcesJar()
                        withJavadocJar()
                    }
                }
            }

            afterEvaluate {
                dependencies {
                    if (project.name != "core") {
                        api(project(":core"))
                    }

                    implementation(catalog.findLibrary("startup-runtime").get())
                    implementation(catalog.findLibrary("androidx-core-ktx").get())
                }
            }
        }
    }
}