plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(libs.allaymc.api)
    compileOnly(projects.plugin.composeLib)
}

kotlin {
    jvmToolchain(21)
}
