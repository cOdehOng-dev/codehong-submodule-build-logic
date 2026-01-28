package com.codehong.core.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.getByType
import java.util.Properties

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.getVersion(alias: String): Int {
    return this.findVersion(alias).get().toString().toInt()
}

fun VersionCatalog.getLibrary(alias: String): Provider<MinimalExternalModuleDependency> {
    return this.findLibrary(alias).get()
}

fun VersionCatalog.getBundle(alias: String): Provider<ExternalModuleDependencyBundle> {
    return this.findBundle(alias).get()
}

fun VersionCatalog.getPluginId(alias: String): String {
    return this.findPlugin(alias).get().get().pluginId
}

fun Project.getLocalProperty(
    name: String,
    default: String
): String {
    val localProperties = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            load(file.inputStream())
        }
    }
    return try {
        if (localProperties.containsKey(name)) {
            localProperties[name].toString()
        } else {
            default
        }
    } catch (e: Exception) {
        e.printStackTrace()
        default
    }
}

fun Project.getStringProperty(
    name: String,
    default: String
): String {
    return try {
        if (this.properties.containsKey(name)) {
            this.properties[name].toString()
        } else {
            default
        }
    } catch (e: Exception) {
        e.printStackTrace()
        default
    }
}

fun Project.getIntProperty(
    name: String,
    default: Int
): Int {
    return try {
        if (this.properties.containsKey(name)) {
            this.properties[name].toString().toInt()
        } else {
            default
        }
    } catch (e: Exception) {
        e.printStackTrace()
        default
    }
}

fun Project.isBooleanProperty(
    name: String,
    default: Boolean
): Boolean {
    return try {
        if (this.properties.containsKey(name)) {
            this.properties[name].toString() == "true"
        } else {
            default
        }
    } catch (e: Exception) {
        e.printStackTrace()
        default
    }
}

fun MavenPublication.pom(
    rootProject: Project,
    project: Project
) {
    pom.withXml {
        val dependenciesNode = asNode().appendNode("dependencies")
        val dependenciesManagementNode = asNode()
            .appendNode("dependencyManagement")
            .appendNode("dependencies")

        project.configurations.getByName("implementation").allDependencies.forEach {
            if (it.group != rootProject.name && it.version != "unspecified") {
                // firebase-bom을 이용하는 경우 필수!!
                if (it.name.endsWith("-bom")) {
                    val dependencyNode = dependenciesManagementNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    dependencyNode.appendNode("version", it.version)
                    dependencyNode.appendNode("scope", "import")
                    dependencyNode.appendNode("type", "pom")
                } else if (it.group != null) {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    if (it.version != null) {
                        dependencyNode.appendNode("version", it.version)
                    }
                }
            }
        }

        project.configurations.getByName("releaseImplementation").allDependencies.forEach {
            if (it.group != rootProject.name && it.version != "unspecified") {
                // firebase-bom을 이용하는 경우 필수!!
                if (it.name.endsWith("-bom")) {
                    val dependencyNode = dependenciesManagementNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    dependencyNode.appendNode("version", it.version)
                    dependencyNode.appendNode("scope", "import")
                    dependencyNode.appendNode("type", "pom")
                } else if (it.group != null) {
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", it.group)
                    dependencyNode.appendNode("artifactId", it.name)
                    if (it.version != null) {
                        dependencyNode.appendNode("version", it.version)
                    }
                }
            }
        }

        val mavenPlugin = asNode()
            .appendNode("build")
            .appendNode("plugins")
            .appendNode("plugin")
        mavenPlugin.apply {
            appendNode("artifactId", "kotlin-maven-plugin")
            appendNode("groupId", "org.jetbrains.kotlin")
            appendNode("version", project.libs.findVersion("kotlin").get().toString())
        }

        val kaptExecution = mavenPlugin
            .appendNode("executions")
            .appendNode("execution")
            .apply {
                appendNode("id", "kapt")
            }
        kaptExecution.appendNode("goals").apply {
            appendNode("goal", "kapt")
        }

        val configuration = kaptExecution.appendNode("configuration")
        configuration.appendNode("sourceDirs").apply {
            appendNode("sourceDir", "src/main/java")
        }

        val annotationProcessorPaths =
            configuration.appendNode("annotationProcessorPaths")
        project.configurations.getByName("kapt").allDependencies.forEach {
            annotationProcessorPaths.appendNode("annotationProcessorPath").apply {
                appendNode("groupId", it.group)
                appendNode("artifactId", it.name)
                appendNode("version", it.version)
            }
        }
    }
}