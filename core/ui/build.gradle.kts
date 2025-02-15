plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.android.compose)
}

android {
    namespace = "com.gowoon.ui"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
}