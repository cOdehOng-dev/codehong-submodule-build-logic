package com.codehong.core.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal fun Project.configureApplicationFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        flavorDimensions += "mode"

        extensions.configure<ApplicationExtension> {
            val appId = project.getStringProperty("APP_ID", "")
            val appName = project.getStringProperty("APP_NAME", "")
            val injectVersionCode = project.getIntProperty("VERSION_CODE", 0)
            val date = SimpleDateFormat("MMddHHmm", Locale.KOREA).format(Date()).toInt()

            productFlavors {
                create("dev") {
                    dimension = "mode"
                    sourceSets.getByName("dev").res.srcDirs("src/dev/res")

                    applicationIdSuffix = ".dev"
                    manifestPlaceholders["appId"] = "${appId}.dev"
                    manifestPlaceholders["appName"] = "${appName}-dev"

                    versionCode = injectVersionCode + date
                }

                create("prod") {
                    dimension = "mode"

                    applicationIdSuffix = ""
                    manifestPlaceholders["appId"] = appId
                    manifestPlaceholders["appName"] = appName

                    versionCode = injectVersionCode
                }
            }
        }
    }
}

internal fun Project.configureLibraryFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        flavorDimensions += "mode"

        extensions.configure<LibraryExtension> {}
    }
}
