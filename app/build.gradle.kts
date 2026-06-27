plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

val koinProxyMajor = providers.gradleProperty("koinProxyMajor").orElse("3").get()
val koinProxyModule = when (koinProxyMajor) {
    "3" -> ":koin-3-proxy"
    "4" -> ":koin-4-proxy"
    else -> error("Unsupported koinProxyMajor=$koinProxyMajor. Use 3 or 4.")
}
val koinDirectExampleModule = when (koinProxyMajor) {
    "3" -> ":example-with-direct-koin-3"
    "4" -> ":example-with-direct-koin-4"
    else -> error("Unsupported koinProxyMajor=$koinProxyMajor. Use 3 or 4.")
}

android {
    namespace = "io.github.twiceyuan.koin.proxy"
    compileSdk {
        version = release(37) {
            minorApiLevel = 0
        }
    }

    defaultConfig {
        applicationId = "io.github.twiceyuan.koin.proxy"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(koinDirectExampleModule))
    implementation(project(":example-with-koin-proxy"))
    implementation(project(koinProxyModule))
    implementation(project(":koin-proxy-api"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
