plugins {
    alias(libs.plugins.donmani.android.library)
    alias(libs.plugins.donmani.hilt)
}

android {
    namespace = "com.gowoon.data"
}

dependencies {
    implementation(projects.core.network)
    implementation(projects.core.datastore)
    implementation(projects.core.domain)
}