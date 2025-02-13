plugins {
    alias(libs.plugins.donmani.kotlin.jvm.library)
    alias(libs.plugins.donmani.hilt)
}

dependencies {
    implementation(projects.core.model)
}