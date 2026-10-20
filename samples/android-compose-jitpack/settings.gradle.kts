pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // Candidate mode accepts only an explicitly selected file repository.
        // Published mode never consults mavenLocal or a candidate SDK repository.
        val candidateRepository = providers.gradleProperty("m3SdkRepository").orNull
        exclusiveContent {
            forRepository { maven { url = uri(candidateRepository ?: "https://jitpack.io") } }
            filter { includeGroupByRegex("com\\.github\\.m3mobile(\\..*)?") }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "M3SdkAndroidComposeSample"
include(":app")
