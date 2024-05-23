plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation ("com.amazonaws:aws-android-sdk-s3:2.28.2")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.mapbox.maps:android:11.4.0")
                implementation("com.mapbox.extension:maps-compose:11.4.0")
            }
        }
    }
}

android {
    namespace = "com.example.mapboxprototype"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}