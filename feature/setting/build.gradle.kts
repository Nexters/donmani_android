import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.setting"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "NOTICE_URL",
            gradleLocalProperties(rootDir, providers).getProperty("PRIVATE_PRIVACY_URL")
        )
        buildConfigField(
            "String",
            "PRIVATE_PRIVACY_URL",
            gradleLocalProperties(rootDir, providers).getProperty("PRIVATE_PRIVACY_URL")
        )
        buildConfigField(
            "String",
            "FEEDBACK_URL",
            gradleLocalProperties(rootDir, providers).getProperty("FEEDBACK_URL")
        )
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}