package com.codehong.core.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

//        testOptions {
//            unitTests {
//                // For Robolectric
//                isIncludeAndroidResources = true
//            }
//        }

        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                freeCompilerArgs.addAll(
                    // -Xopt-in은 특정 기능을 사용할 것을 명시적으로 선언하는 데 사용되는 옵션으로,
                    // 실험적 API 컴파일 시 빌드가 중단되지 않도록 설정
                    arrayListOf<String>().apply {
//                        add("-Xopt-in=androidx.compose.ui.util.ExperimentalComposeUiApi")
//                        add("-Xopt-in=androidx.compose.foundation.ExperimentalFoundationApi")
                        add("-Xopt-in=androidx.compose.material3.ExperimentalMaterial3Api")
                        add("-Xopt-in=androidx.compose.material.ExperimentalMaterialApi")
//                        add("-Xopt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi")
//                        add("-Xopt-in=androidx.compose.runtime.saveable.ExperimentalSaveableApi")
                    }
                )
            }
        }
    }

    dependencies {
        val bom = libs.getLibrary("androidx-compose-bom")
        add("implementation", platform(bom))
        add("androidTestImplementation", platform(bom))

        add("implementation", libs.getLibrary("androidx-activity-compose"))
        add("implementation", libs.getLibrary("androidx-compose-ui"))
        add("implementation", libs.getLibrary("androidx-compose-ui-graphics"))
        add("implementation", libs.getLibrary("androidx-compose-ui-tooling-preview"))
        add("implementation", libs.getLibrary("androidx-compose-material3"))
        add("implementation", libs.getLibrary("androidx-compose-foundation"))
        add("implementation", libs.getLibrary("androidx-compose-viewmodel"))
        add("implementation", libs.getLibrary("androidx-compose-runtime-livedata"))
        add("implementation", libs.getLibrary("androidx-compose-foundation-layout-android"))
        add("implementation", libs.getLibrary("androidx-compose-material3-adaptive"))
        add("implementation", libs.getLibrary("androidx-material-icons-extended"))

        add("implementation", libs.getLibrary("google-accompanist-permissions"))
        add("implementation", libs.getLibrary("google-accompanist-flowlayout"))
        add("implementation", libs.getLibrary("google-accompanist-pager"))
        add("implementation", libs.getLibrary("google-accompanist-pager-indicators"))

        add("implementation", libs.getBundle("coil"))
        add("implementation", libs.getBundle("glide"))

        add("debugImplementation", libs.getLibrary("androidx-compose-ui-tooling"))
        add("debugImplementation", libs.getLibrary("androidx-compose-ui-test-manifest"))

        add("androidTestImplementation", libs.getLibrary("androidx-compose-ui-test-junit4"))
    }
}