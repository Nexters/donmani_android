plugins {
    alias(libs.plugins.donmani.android.library)
}

android {
    namespace = "com.gowoon.common"
}

dependencies {
    implementation(libs.androidx.lifecycle.viewmodel)
}