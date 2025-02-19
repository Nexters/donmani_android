plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.setting"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}