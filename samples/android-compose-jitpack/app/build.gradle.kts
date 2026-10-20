plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// Resolve the consumer's actual graph so a project dependency cannot masquerade as a package test.
val verifyM3SdkDependency = tasks.register("verifyM3SdkDependency") {
    doLast {
        val result = configurations.getByName("releaseRuntimeClasspath").incoming.resolutionResult
        val rootId = result.root.id
        check(result.allComponents.none {
            it.id is org.gradle.api.artifacts.component.ProjectComponentIdentifier && it.id != rootId
        }) { "Package verification forbids SDK project dependencies" }
        val artifact = if (providers.gradleProperty("m3SdkRepository").isPresent) "sdk" else "M3SDK"
        check(result.allComponents.any {
            val id = it.id
            id is org.gradle.api.artifacts.component.ModuleComponentIdentifier &&
                id.group == "com.github.m3mobile" && id.module == artifact &&
                id.version == providers.gradleProperty("m3SdkVersion").get()
        }) { "Expected SDK package/version not resolved" }
    }
}

tasks.matching { it.name == "preReleaseBuild" }.configureEach {
    dependsOn(verifyM3SdkDependency)
}

val m3SdkVersion = providers.gradleProperty("m3SdkVersion").orNull
    ?: error("Provide -Pm3SdkVersion=<released JitPack tag>. Local SDK modules are intentionally unsupported.")

android {
    namespace = "net.m3mobile.samples.compose"
    compileSdk = 36

    defaultConfig {
        applicationId = "net.m3mobile.samples.compose"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "M3_SDK_VERSION", "\"$m3SdkVersion\"")
    }

    buildTypes {
        release {
            // Verification APK only; installable without a separate signing/rebuild step.
            signingConfig = signingConfigs.getByName("debug")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    val m3SdkArtifact = if (providers.gradleProperty("m3SdkRepository").isPresent) "sdk" else "M3SDK"
    implementation("com.github.m3mobile:$m3SdkArtifact:$m3SdkVersion")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    debugImplementation("androidx.compose.ui:ui-tooling")
    testImplementation("junit:junit:4.13.2")
}
