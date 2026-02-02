plugins {
    `kotlin-dsl`
}

group = "com.codehong.core.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.codehong.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = libs.plugins.codehong.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.codehong.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = libs.plugins.codehong.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidBuildType") {
            id = libs.plugins.codehong.android.build.type.get().pluginId
            implementationClass = "AndroidBuildTypeConventionPlugin"
        }
        register("androidFlavor") {
            id = libs.plugins.codehong.android.flavor.get().pluginId
            implementationClass = "AndroidFlavorConventionPlugin"
        }
        register("androidFirebase") {
            id = libs.plugins.codehong.android.firebase.get().pluginId
            implementationClass = "AndroidFirebaseConventionPlugin"
        }
        register("androidHilt") {
            id = libs.plugins.codehong.android.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
        register("androidRoom") {
            id = libs.plugins.codehong.android.room.get().pluginId
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidNetwork") {
            id = libs.plugins.codehong.android.network.get().pluginId
            implementationClass = "NetworkLibraryConventionPlugin"
        }
        register("androidLibraryPublishing") {
            id = libs.plugins.codehong.android.library.publishing.get().pluginId
            implementationClass = "PublishingLibraryConventionPlugin"
        }
        register("androidLibraryBomPublishing") {
            id = libs.plugins.codehong.android.library.bom.publishing.get().pluginId
            implementationClass = "PublishingLibraryBomConventionPlugin"
        }
    }
}