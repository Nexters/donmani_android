import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.android.compose)
    alias(libs.plugins.donmani.hilt)
    alias(libs.plugins.donmani.kotlin.serialization)
}

android {
    namespace = "com.gowoon.ui"
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            buildConfigField(
                "String",
                "AD_UNIT_ID",
                gradleLocalProperties(rootDir, providers).getProperty("AD_UNIT_ID_PRODUCT")
            )
        }
        debug {
            buildConfigField(
                "String",
                "AD_UNIT_ID",
                gradleLocalProperties(rootDir, providers).getProperty("AD_UNIT_ID_TEST")
            )
        }
    }
}

dependencies {
    implementation(libs.physics.layout.compose)
    implementation(libs.lottie.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.common)
}