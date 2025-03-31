import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.statistics"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "REQUEST_URL",
            gradleLocalProperties(rootDir, providers).getProperty("REQUEST_URL")
        )
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}