import com.android.build.api.dsl.ApplicationExtension
import com.codehong.core.convention.configureKotlinAndroid
import com.codehong.core.convention.getPluginId
import com.codehong.core.convention.getVersion
import com.codehong.core.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            apply(plugin = libs.getPluginId("android-application"))
            apply(plugin = libs.getPluginId("kotlin-android"))
            apply(plugin = libs.getPluginId("kotlin-kapt"))
            apply(plugin = libs.getPluginId("kotlin-parcelize"))
            apply(plugin = libs.getPluginId("ksp"))

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    targetSdk = libs.getVersion("targetSdk")
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
