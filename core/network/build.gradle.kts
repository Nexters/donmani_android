import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.hilt)
    alias(libs.plugins.donmani.kotlin.serialization)
}

android {
    namespace = "com.gowoon.network"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "SERVER_URL",
            gradleLocalProperties(rootDir, providers).getProperty("SERVER_URL")
        )
        buildConfigField(
            "String",
            "SERVER_VERSION",
            "\"${libs.versions.apiVersion.get()}\""
        )
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
}