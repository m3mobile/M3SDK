pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.9.0")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "M3SDK"
include(":app")
include(":core")
include(":sdk-processor")
include(":sdk")
include(":sdk:startup")
include(":sdk:keytool")
include(":sdk:scanemul")
