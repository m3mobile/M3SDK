plugins {
    alias(libs.plugins.sdk.common)
}

dependencies {
    testImplementation(libs.junit)
    testImplementation("org.robolectric:robolectric:4.12.2")
}

tasks.withType<Test>().configureEach {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
