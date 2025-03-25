rootProject.name = "donmani_android"

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":feature:home")
include(":feature:record")
include(":core:domain")
include(":core:model")
include(":core:designsystem")
include(":core:data")
include(":core:network")
include(":core:common")
include(":core:ui")
include(":core:datastore")
include(":feature:setting")
include(":feature:recordlist")
include(":feature:onboarding")
include(":feature:splash")
include(":feature:starbottlelist")
