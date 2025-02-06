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
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.ui)
    implementation(projects.feature.home)
    implementation(projects.feature.mypage)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.android.ui.test)
}