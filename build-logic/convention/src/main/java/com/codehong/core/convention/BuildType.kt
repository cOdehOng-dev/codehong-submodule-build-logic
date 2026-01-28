package com.codehong.core.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.util.Properties

internal fun Project.configureApplicationBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        extensions.configure<ApplicationExtension> {
            buildTypes {
                debug {
                    isMinifyEnabled = false
                    isCrunchPngs = false // PNG 리소스 압축 활성화

                    tasks.whenTaskAdded {
                        if (name == "lint") {
                            enabled = false
                        }
                    }

                    proguardFile("proguard-rules.pro")
                }

                release {
                    isMinifyEnabled = true
                    isCrunchPngs = false
                    proguardFile("proguard-rules.pro")

                    if (project.isBooleanProperty("ENABLE_SIGNING_CONFIGS", false)) {
                        isDebuggable = true
                        signingConfig = signingConfigs.create("release") {
                            val keystorePropertiesFile = rootProject.file("signingconfig.properties")
                            val keystoreProperties = Properties()
                            keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                            storeFile = file(keystoreProperties["storeFile"] ?: "")
                            storePassword = keystoreProperties["storePassword"] as String
                            keyAlias = keystoreProperties["keyAlias"] as String
                            keyPassword = keystoreProperties["keyPassword"] as String
                        }
                    }
                }
            }
        }
    }
}

internal fun Project.configureLibraryBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        val versionName = project.getStringProperty("VERSION_NAME", "")

        extensions.configure<LibraryExtension> {
            buildTypes {
                debug {
                    tasks.whenTaskAdded {
                        if (name == "lint") {
                            enabled = false
                        }
                    }

                    buildConfigField(
                        "String",
                        "VERSION_NAME",
                        "\"$versionName\""
                    )
                }

                release {
                    isMinifyEnabled = false

                    buildConfigField(
                        "String",
                        "VERSION_NAME",
                        "\"$versionName\""
                    )
                }
            }
        }
    }
}