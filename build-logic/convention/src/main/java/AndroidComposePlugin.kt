import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.gowoon.configs.Plugins
import com.gowoon.configs.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidComposePlugin: Plugin<Project>{
    override fun apply(target: Project) {
        with(target){
            apply(plugin = Plugins.KOTLIN_ANDROID)
            apply(plugin = Plugins.KOTLIN_COMPOSE)

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }

}