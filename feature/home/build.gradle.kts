plugins {
    alias(libs.plugins.donmani.android.feature)
}

android {
    namespace = "com.gowoon.home"
}

dependencies {
    implementation(libs.play.review)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}