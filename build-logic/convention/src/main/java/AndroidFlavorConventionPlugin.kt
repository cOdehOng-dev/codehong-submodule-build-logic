import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.codehong.core.convention.configureApplicationFlavors
import com.codehong.core.convention.configureLibraryFlavors
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidFlavorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin(libs.getPluginId("android-application")) -> {
                    configure<ApplicationExtension> {
                        configureApplicationFlavors(this)
                    }
                }

                pluginManager.hasPlugin(libs.getPluginId("android-library")) -> {
                    configure<LibraryExtension> {
                        configureLibraryFlavors(this)
                    }
                }

                else -> {}
            }
        }
    }
}