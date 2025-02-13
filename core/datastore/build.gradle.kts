plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.hilt)
}

android {
    namespace = "com.gowoon.datastore"
}

dependencies {
    implementation(libs.androidx.datastore.preference)
}