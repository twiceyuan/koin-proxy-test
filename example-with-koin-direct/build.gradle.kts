plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    val koinProxyMajor = providers.gradleProperty("koinProxyMajor").orElse("3").get()
    when (koinProxyMajor) {
        "3" -> implementation(libs.koin3.core)
        "4" -> {
            implementation(platform(libs.koin4.bom))
            implementation(libs.koin4.core)
        }
        else -> error("Unsupported koinProxyMajor=$koinProxyMajor. Use 3 or 4.")
    }
}
