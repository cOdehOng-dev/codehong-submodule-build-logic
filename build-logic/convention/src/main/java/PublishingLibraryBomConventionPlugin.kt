import com.codehong.core.convention.pom
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.register
import java.util.Properties

class PublishingLibraryBomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        with(target) {
            val libraryGroupId = rootProject.properties["GROUP_ID"] as String
            val libraryVersion = rootProject.properties["VERSION_NAME"] as String

            /**
             * task 이름을 "publishReleaseToGitHub"로 설정
             * task를 github 그룹으로 설정
             * assembleRelease로 라이브러리 aar 빌드 + 해당 task를 assembleRelease에 의존하도록 설정 (= assembleRelease가 먼저 실행되어야 함)
             * assembleRelease가 끝난 후에 publishReleasePublicationToGitHubPackagesRepository를 실행하도록 설정
             */
            task("publishReleaseToGitHub") {
                group = "github"
                dependsOn("assembleRelease")
                finalizedBy("publishReleasePublicationToGitHubPackagesRepository")
            }

            task("publishSnapshotToGitHub") {
                group = "github"
                dependsOn("assembleRelease")
                finalizedBy("publishSnapshotPublicationToGitHubPackagesRepository")
            }

            task("publishSnapshotToLocal") {
                group = "github"
                dependsOn("assembleRelease")
                finalizedBy("publishSnapshotLocalPublicationToMavenLocal")
            }

            pluginManager.apply("maven-publish")

            extensions.configure<PublishingExtension> {
                repositories {
                    maven {
                        val githubProperties = Properties().apply {
                            load(rootProject.file("github.properties").inputStream())
                        }

                        name = "GitHubPackages"
                        url = uri(githubProperties["url"] as String)
                        credentials {
                            username = githubProperties["username"] as String
                            password = githubProperties["token"] as String
                        }
                    }
                }

                publications {
                    val libraryArtifact = project.properties["ARTIFACT"] as String
                    val libraryArtifactId = target.properties["ARTIFACT_ID"] as String

                    register<MavenPublication>("release") {
                        groupId = libraryGroupId
                        artifactId = libraryArtifactId
                        version = libraryVersion

                        afterEvaluate {
                            pom(rootProject, project)
                            artifact(libraryArtifact)
                        }
                    }

                    register<MavenPublication>("snapshot") {
                        groupId = libraryGroupId
                        artifactId = libraryArtifactId

                        /**
                         * 멀티 모듈을 모두 같은 버전으로 올리기 위한 처리
                         */
                        version = if (rootProject.extra.has("BOM_VERSION_NAME")) {
                            rootProject.extra["BOM_VERSION_NAME"] as String
                        } else {
                            "$libraryVersion-SNAPSHOT"
                        }

                        afterEvaluate {
                            pom(rootProject, project)
                            artifact(libraryArtifact)
                        }
                    }

                    register<MavenPublication>("snapshotLocal") {
                        groupId = libraryGroupId
                        artifactId = libraryArtifactId
                        version =  "$libraryVersion-SNAPSHOT"

                        afterEvaluate {
                            pom(rootProject, project)
                            artifact(libraryArtifact)
                        }
                    }
                }
            }
        }
    }
}