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
                implementation("com.mapbox.maps:android:11.4.0")
                implementation("io.ktor:ktor-client-core:2.3.3")
                implementation("com.squareup.sqldelight:runtime:1.5.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.mapbox.maps:android:11.4.0")
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