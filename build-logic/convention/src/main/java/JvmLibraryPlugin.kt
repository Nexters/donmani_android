import com.gowoon.configs.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class JvmLibraryPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply(plugin = "org.jetbrains.kotlin.jvm")
            configureKotlinJvm()
        }
    }
}