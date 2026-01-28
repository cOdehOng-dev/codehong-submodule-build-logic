
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.codehong.core.convention.getLibrary
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.libs
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.getPluginId("google-services"))
            apply(plugin = libs.getPluginId("firebase-crashlytics"))

            dependencies {
                add("implementation", platform(libs.getLibrary("firebase-bom")))
                add("implementation", libs.getLibrary("firebase-crashlytics"))
            }

            when {
                pluginManager.hasPlugin(libs.getPluginId("android-application")) -> {
                    configure<ApplicationExtension> {
                        buildTypes.getByName("release") {
                            configure<CrashlyticsExtension> {
                                mappingFileUploadEnabled = true
                            }
                        }

                        buildTypes.getByName("debug") {
                            configure<CrashlyticsExtension> {
                                mappingFileUploadEnabled = false
                            }
                        }
                    }
                }

                pluginManager.hasPlugin(libs.getPluginId("android-library")) -> {
                    configure<LibraryExtension> {}
                }

                else -> {}
            }
        }
    }
}