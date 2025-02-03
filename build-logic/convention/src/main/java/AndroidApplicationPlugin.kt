import com.android.build.api.dsl.ApplicationExtension
import com.gowoon.configs.Plugins
import com.gowoon.configs.configureAndroidCompose
import com.gowoon.configs.configureKotlinAndroid
import com.gowoon.configs.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply(plugin = Plugins.ANDROID_APPLICATION)
            apply(plugin = Plugins.KOTLIN_ANDROID)
            apply(plugin = Plugins.KOTLIN_COMPOSE)

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                defaultConfig {
                    applicationId = libs.findVersion("applicationId").get().toString()
                    targetSdk = libs.findVersion("targetSdk").get().requiredVersion.toInt()
                    versionCode = libs.findVersion("versionCode").get().requiredVersion.toInt()
                    versionName = libs.findVersion("versionName").get().toString()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
        }
    }
}