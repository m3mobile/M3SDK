plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
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
    val m3SdkArtifact = if (m3SdkVersion.endsWith("-local")) "sdk" else "M3SDK"
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
