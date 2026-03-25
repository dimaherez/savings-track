plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.dmytroherez.savingstrack"
version = "unspecified"

kotlin {
    jvm()
    androidTarget()

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.serialization.kotlinx.json)
        }
    }
}

android {
    namespace = "com.dmytroherez.savingstrack.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
