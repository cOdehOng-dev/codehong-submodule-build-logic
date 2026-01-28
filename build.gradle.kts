import java.text.SimpleDateFormat
import java.util.Date

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}

ext {
    settingLibraryBomSnapshotVersion()
}

fun settingLibraryBomSnapshotVersion() {
    if (!rootProject.hasProperty("VERSION_NAME")) return
    val libraryVersion = rootProject.properties["VERSION_NAME"] as String

    val timestamp = SimpleDateFormat("yyyyMMdd.HHmmss").format(Date())
    rootProject.extra["BOM_VERSION_NAME"] = "$libraryVersion-${timestamp}-SNAPSHOT"
}