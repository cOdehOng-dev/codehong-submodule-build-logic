import com.codehong.core.convention.getLibrary
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.getPluginId("ksp"))

            dependencies {
                add("implementation", libs.getLibrary("androidx-room-ktx"))
                add("implementation", libs.getLibrary("androidx-room-runtime"))
                add("ksp", libs.getLibrary("androidx-room-compiler"))
                add("debugImplementation", libs.getLibrary("androidx-room-testing"))
            }
        }
    }
}