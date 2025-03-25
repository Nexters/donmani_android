plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.starbottlelist"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}