plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(projects.plugin.kotlinx)
    compileOnly(projects.plugin.stdlib)
    compileOnly(projects.plugin.compose)
}

kotlin {
    jvmToolchain(21)
}
