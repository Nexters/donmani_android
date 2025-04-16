plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.hilt)
    alias(libs.plugins.donmani.kotlin.serialization)
}

android {
    namespace = "com.gowoon.common"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.bundles.firebase)
}