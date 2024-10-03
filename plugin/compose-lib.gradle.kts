import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.gradleup.shadow)
}

@Suppress("DEPRECATION")
buildDir = file("../build")

@OptIn(ExperimentalComposeLibrary::class)
dependencies {
    api(projects.plugin)

    api(compose.runtime)
    api(compose.ui)
    api(compose.foundation)
    api(compose.material)
    api(compose.material3)
    api(compose.materialIconsExtended)
    api(compose.animation)
    api(compose.animationGraphics)
    api(compose.runtimeSaveable)
    api(compose.components.resources)
    api(compose.components.uiToolingPreview)
    api(compose.desktop.linux_x64)
    api(compose.desktop.linux_arm64)
    api(compose.desktop.macos_x64)
    api(compose.desktop.macos_arm64)
    api(compose.desktop.windows_x64)
    api(compose.desktop.components.splitPane)
    api(compose.desktop.components.animatedImage)
}

kotlin {
    jvmToolchain(21)
}
