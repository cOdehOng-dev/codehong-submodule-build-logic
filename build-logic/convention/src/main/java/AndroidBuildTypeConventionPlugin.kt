import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.codehong.core.convention.configureApplicationBuildTypes
import com.codehong.core.convention.configureLibraryBuildTypes
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidBuildTypeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            when {
                pluginManager.hasPlugin(libs.getPluginId("android-application")) -> {
                    configure<ApplicationExtension> {
                        configureApplicationBuildTypes(this)
                    }
                }

                pluginManager.hasPlugin(libs.getPluginId("android-library")) -> {
                    configure<LibraryExtension> {
                        configureLibraryBuildTypes(this)
                    }
                }

                else -> {}
            }
        }
    }
}