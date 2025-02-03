plugins {
    `kotlin-dsl`
}

group = "com.gowoon.convention.buildlogic"


dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication"){
            id = libs.plugins.donmani.android.application.get().pluginId
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidCompose"){
            id = libs.plugins.donmani.android.compose.get().pluginId
            implementationClass = "AndroidComposePlugin"
        }
        register("androidLibrary"){
            id = libs.plugins.donmani.android.library.get().pluginId
            implementationClass = "AndroidLibraryPlugin"
        }
        register("kotlinJvmLibrary"){
            id = libs.plugins.donmani.kotlin.jvm.library.get().pluginId
            implementationClass = "JvmLibraryPlugin"
        }
        register("androidFeature"){
            id = libs.plugins.donmani.android.feature.get().pluginId
            implementationClass = "AndroidFeaturePlugin"
        }
    }
}
