plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(libs.allaymc.api)
    api(projects.core)

    api(kotlin("stdlib"))
    api(kotlin("stdlib-jdk7"))
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.swing)
}

kotlin {
    jvmToolchain(21)
}
