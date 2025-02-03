plugins {
    alias(libs.plugins.donmani.kotlin.jvm.library)
}

dependencies {
    implementation(projects.core.model)

    testImplementation(libs.junit)
}