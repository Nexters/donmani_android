import com.android.build.api.dsl.LibraryExtension
import com.gowoon.configs.Plugins
import com.gowoon.configs.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLibraryPlugin: Plugin<Project>{
    override fun apply(target: Project) {
        with(target){
            apply(plugin = Plugins.ANDROID_LIBRARY)
            apply(plugin = Plugins.KOTLIN_ANDROID)

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
            }
        }
    }
}