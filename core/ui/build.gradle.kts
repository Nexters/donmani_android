plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.android.compose)
    alias(libs.plugins.donmani.hilt)
    alias(libs.plugins.donmani.kotlin.serialization)
}

android {
    namespace = "com.gowoon.ui"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.common)
}