plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.fortune"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}
