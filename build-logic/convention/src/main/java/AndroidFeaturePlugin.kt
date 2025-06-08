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

            dependencies {
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:model"))
                "implementation"(project(":core:common"))

                "implementation"(libs.findLibrary("lottie.compose").get())
                "implementation"(libs.findLibrary("coil.compose").get())
                "implementation"(libs.findLibrary("coil.network").get())
                "implementation"(libs.findLibrary("androidx.media.exoplayer").get())

                "implementation"(libs.findLibrary("androidx.navigation.compose").get())
                "implementation"(libs.findBundle("android-lifecycle").get())
            }
        }

    }
}