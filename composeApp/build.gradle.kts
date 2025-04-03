import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                // Koroutines
                implementation(libs.kotlinx.coroutines.core)
                // AndroidX DataStore
                implementation(libs.androidx.datastore.preferences.core)    // preferences datastore
                // Koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                // Koin
                implementation(libs.koin.android)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                // Koin
                implementation(libs.koin.jvm)

            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {

            }
        }
        val iosX64Main by getting {
            dependsOn(iosMain)
            dependencies {
                // Koin
                implementation(libs.koin.iosx64)
            }
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
            dependencies {
                // Koin
                implementation(libs.koin.iosarm64)

            }
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
            dependencies {
                // Koin
                implementation(libs.koin.iossimulatorarm64)

            }
        }
    }
}

android {
    namespace = "org.ihateham.kotlinmultiplatform"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.ihateham.kotlinmultiplatform"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "org.ihateham.kotlinmultiplatform.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.ihateham.kotlinmultiplatform"
            packageVersion = "1.0.0"
        }
    }
}
