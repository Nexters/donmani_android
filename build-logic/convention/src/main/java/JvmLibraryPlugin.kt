import com.gowoon.configs.configureKotlinJvm
import com.gowoon.configs.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class JvmLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "org.jetbrains.kotlin.jvm")
            configureKotlinJvm()

            dependencies {
                "implementation"(libs.findLibrary("kotlinx.coroutine.core").get())
            }
        }
    }
}