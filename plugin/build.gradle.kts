plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(libs.allaymc.api)
}

kotlin {
    jvmToolchain(21)
}
