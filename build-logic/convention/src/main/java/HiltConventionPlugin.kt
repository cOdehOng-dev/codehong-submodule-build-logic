import com.codehong.core.convention.getLibrary
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.getPluginId("ksp"))
            apply(plugin = libs.getPluginId("android-hilt"))

            dependencies {
                add("implementation", libs.getLibrary("hilt-android"))
                add("ksp", libs.getLibrary("hilt-compiler"))
                add("ksp", libs.getLibrary("hilt-android-compiler"))

                add("androidTestImplementation", libs.getLibrary("hilt-android-testing"))
                add("kspAndroidTest", libs.getLibrary("hilt-android-compiler"))
            }
        }
    }
}