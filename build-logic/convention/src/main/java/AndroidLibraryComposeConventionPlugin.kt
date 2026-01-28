import com.android.build.api.dsl.LibraryExtension
import com.codehong.core.convention.configureAndroidCompose
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidLibraryComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply(plugin = libs.getPluginId("android-library"))
            apply(plugin = libs.getPluginId("kotlin-compose-compiler"))

            extensions.configure<LibraryExtension> {
                configureAndroidCompose(this)
            }
        }
    }
}