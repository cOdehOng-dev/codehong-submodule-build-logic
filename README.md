# build-logic

Android 프로젝트를 위한 Gradle Convention Plugin 모듈입니다. 공통 빌드 설정을 중앙화하여 멀티 모듈 프로젝트에서 일관된 빌드 구성을 제공합니다.

## 📋 개요

build-logic은 Android Application/Library 모듈에서 반복되는 Gradle 설정을 Convention Plugin으로 추상화하여 제공합니다. Kotlin DSL 기반으로 작성되었으며, Version Catalog를 활용하여 의존성을 관리합니다.

## 🏗️ 구조

```
build-logic/
├── settings.gradle.kts
├── gradle.properties
└── convention/
    ├── build.gradle.kts
    └── src/main/java/
        ├── AndroidApplicationConventionPlugin.kt
        ├── AndroidApplicationComposeConventionPlugin.kt
        ├── AndroidLibraryConventionPlugin.kt
        ├── AndroidLibraryComposeConventionPlugin.kt
        ├── AndroidBuildTypeConventionPlugin.kt
        ├── AndroidFlavorConventionPlugin.kt
        ├── AndroidFirebaseConventionPlugin.kt
        ├── HiltConventionPlugin.kt
        ├── AndroidRoomConventionPlugin.kt
        ├── PublishingLibraryConventionPlugin.kt
        ├── PublishingLibraryBomConventionPlugin.kt
        └── com/codehong/core/convention/
            ├── AndroidCompose.kt
            ├── KotlinAndroid.kt
            ├── Flavor.kt
            ├── BuildType.kt
            └── BuildLogicExtensions.kt
```

## 📦 주요 플러그인

### AndroidApplicationConventionPlugin
Android Application 모듈의 기본 설정을 구성합니다.
- `android-application`, `kotlin-android`, `kotlin-kapt`, `kotlin-parcelize`, `ksp` 플러그인 적용
- targetSdk 설정 및 Kotlin Android 공통 설정 적용

### AndroidApplicationComposeConventionPlugin
Application 모듈에 Jetpack Compose 설정을 추가합니다.
- Compose Compiler 플러그인 적용
- Compose BOM 및 관련 라이브러리 의존성 추가

### AndroidLibraryConventionPlugin
Android Library 모듈의 기본 설정을 구성합니다.
- `android-library`, `kotlin-android`, `kotlin-kapt`, `kotlin-parcelize`, `ksp` 플러그인 적용
- testInstrumentationRunner, consumerProguardFiles 설정

### AndroidLibraryComposeConventionPlugin
Library 모듈에 Jetpack Compose 설정을 추가합니다.

### AndroidBuildTypeConventionPlugin
Application/Library 모듈의 Build Type(debug/release)을 구성합니다.
- debug: minify 비활성화, lint 비활성화
- release: minify 활성화, signing config 설정 (옵션)

### AndroidFlavorConventionPlugin
Product Flavor(dev/prod)를 구성합니다.
- dev: applicationIdSuffix `.dev`, 개발용 앱 이름
- prod: 프로덕션 설정

### AndroidFirebaseConventionPlugin
Firebase 설정을 구성합니다.
- Google Services, Firebase Crashlytics 플러그인 적용
- release 빌드 시 mapping file 업로드 활성화

### HiltConventionPlugin
Hilt 의존성 주입 설정을 구성합니다.
- `hilt-android`, `hilt-compiler` 의존성 추가
- 테스트용 Hilt 의존성 포함

### AndroidRoomConventionPlugin
Room 데이터베이스 설정을 구성합니다.
- `room-ktx`, `room-runtime`, `room-compiler` 의존성 추가

### PublishingLibraryConventionPlugin
라이브러리를 GitHub Packages에 배포하기 위한 설정을 구성합니다.
- release, snapshot, snapshotLocal publication 등록
- `publishReleaseToGitHub`, `publishSnapshotToGitHub` task 제공

### PublishingLibraryBomConventionPlugin
BOM(Bill of Materials) 라이브러리 배포 설정을 구성합니다.
- POM 파일 자동 생성
- 멀티 모듈 버전 동기화 지원

## 🚀 사용법

### 1. settings.gradle.kts에 build-logic 포함

```kotlin
pluginManagement {
    includeBuild("build-logic")
}
```

### 2. 모듈의 build.gradle.kts에서 플러그인 적용

```kotlin
// Application 모듈
plugins {
    alias(libs.plugins.codehong.android.application)
    alias(libs.plugins.codehong.android.application.compose)
    alias(libs.plugins.codehong.android.hilt)
}

// Library 모듈
plugins {
    alias(libs.plugins.codehong.android.library)
    alias(libs.plugins.codehong.android.library.compose)
}
```

### 3. GitHub Packages 배포

```bash
# Release 배포
./gradlew :module-name:publishReleaseToGitHub

# Snapshot 배포
./gradlew :module-name:publishSnapshotToGitHub

# Local Maven 배포
./gradlew :module-name:publishSnapshotToLocal
```

## ⚙️ 주요 설정값

| 설정 | 값 |
|------|-----|
| Java Version | 17 |
| JVM Target | JVM_17 |
| compileSdk | Version Catalog에서 관리 |
| minSdk | Version Catalog에서 관리 |
| targetSdk | Version Catalog에서 관리 |

## 📝 참고사항

- `libs.versions.toml` 파일에서 버전 및 플러그인 ID를 관리합니다
- GitHub Packages 배포 시 `github.properties` 파일에 인증 정보가 필요합니다
- Release signing 설정 시 `signingconfig.properties` 파일이 필요합니다
- Flavor 설정 시 `gradle.properties`에 `APP_ID`, `APP_NAME`, `VERSION_CODE` 정의 필요
