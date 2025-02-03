plugins {
    alias(libs.plugins.donmani.android.library)
}

android {
    namespace = "com.gowoon.data"
}

dependencies {
    implementation(projects.core.network)

    testImplementation(libs.junit)
}