import com.gowoon.configs.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply<AndroidLibraryPlugin>()
            apply<AndroidComposePlugin>()
            apply<HiltPlugin>()
            apply<KotlinSerializationPlugin>()
            apply(plugin = "org.jetbrains.kotlin.plugin.parcelize")

            dependencies {
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:common"))

                "implementation"(libs.findLibrary("androidx.core.ktx").get())
                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(libs.findBundle("android-lifecycle").get())
            }
        }

    }
}