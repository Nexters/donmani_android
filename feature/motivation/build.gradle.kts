import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.motivation"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "REVIEW_URL",
            gradleLocalProperties(rootDir, providers).getProperty("REVIEW_URL")
        )
    }
}

dependencies {
    implementation(libs.lottie.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}