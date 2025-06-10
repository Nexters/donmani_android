plugins {
    alias(libs.plugins.donmani.android.application)
}

android {
    namespace = "com.gowoon.donmani_android"
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders["appName"] = "@string/app_name"
        }
        debug {
            applicationIdSuffix = ".dev"
            manifestPlaceholders["appName"] = "@string/app_name_dev"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.feature.home)
    implementation(projects.feature.record)
    implementation(projects.feature.setting)
    implementation(projects.feature.recordlist)
    implementation(projects.feature.onboarding)
    implementation(projects.feature.splash)
    implementation(projects.feature.statistics)
    implementation(projects.feature.starbottlelist)
    implementation(projects.feature.motivation)
    
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}