import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidFeaturePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply<AndroidLibraryPlugin>()
            apply<AndroidComposePlugin>()

            dependencies {
                "implementation"(project(":core:designsystem"))
                "implementation"(project(":core:domain"))
                "implementation"(project(":core:common"))
            }
        }

    }
}