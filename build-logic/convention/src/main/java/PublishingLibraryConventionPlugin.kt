import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

class PublishingLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("maven-publish")

            /**
             * task 이름을 "publishReleaseToGitHub"로 설정
             * task를 github 그룹으로 설정한 후에
             * publishReleasePublicationToGitHubPackagesRepository를 실행
             */
            task("publishReleaseToGitHub") {
                group = "github"
                finalizedBy("publishReleasePublicationToGitHubPackagesRepository")
            }

            task("publishSnapshotToGitHub") {
                group = "github"
                finalizedBy("publishSnapshotPublicationToGitHubPackagesRepository")
            }

            task("publishSnapshotToLocal") {
                group = "github"
                finalizedBy("publishSnapshotLocalPublicationToMavenLocal")
            }

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
                    val libraryGroupId = project.properties["GROUP_ID"] as String
                    val libraryArtifactId = project.properties["ARTIFACT_ID"] as String
                    val libraryVersion = project.properties["VERSION_NAME"] as String

                    register<MavenPublication>("release") {
                        groupId = libraryGroupId
                        artifactId = libraryArtifactId
                        version = libraryVersion

                        afterEvaluate {
                            from(project.components["release"])
                        }
                    }

                    register<MavenPublication>("snapshot") {
                        groupId = libraryGroupId
                        artifactId = libraryArtifactId

                        /**
                         * 버전을 version = "$libraryVersion-SNAPSHOT"로 설정하면
                         * Github Packages에서 업로드시마다 덮혀서 올라감
                         * 따라서 버전 뒤에 현재 시간을 붙여서 모든 버전이 업로드 되도록함
                         */
                        version = libraryVersion +
                                "-${SimpleDateFormat("yyyyMMdd.HHmmss").format(Date())}" +
                                "-SNAPSHOT"

                        afterEvaluate {
                            from(project.components["release"])
                        }
                    }

                    register<MavenPublication>("snapshotLocal") {
                        groupId = libraryGroupId
                        artifactId = libraryArtifactId
                        version =  "$libraryVersion-SNAPSHOT"

                        afterEvaluate {
                            from(project.components["release"])
                        }
                    }
                }
            }
        }
    }
}
