import com.codehong.core.convention.getLibrary
import com.codehong.core.convention.libs
import groovy.lang.Closure
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.exclude

class NetworkLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                add("implementation", platform(libs.getLibrary("okhttp-bom")))
                add("implementation", libs.getLibrary("okhttp"))
                add("implementation", libs.getLibrary("okhttp-logging-interceptor"))
                add("implementation", libs.getLibrary("okhttp-urlconnection"))

                add("implementation", libs.getLibrary("retrofit"))
                add("implementation", libs.getLibrary("retrofit-converter-gson"))
                add("implementation", libs.getLibrary("retrofit-converter-scalars"))
                add("implementation", libs.getLibrary("retrofit-converter-kotlinx-serialization"))
                add(
                    "implementation",
                    libs.getLibrary("retrofit-converter-simplexml"),
                    closureOf<ExternalModuleDependency> {
                        exclude(group = "xpp3", module = "xpp3")
                        exclude(group = "stax", module = "stax-api")
                        exclude(group = "stax", module = "stax")
                    } as Closure<Any>
                )

                add("implementation", libs.getLibrary("gson"))
            }
        }
    }
}