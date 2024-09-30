plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(libs.allaymc.api)
    api(project(":core"))

    api(kotlin("stdlib"))
    api(kotlin("stdlib-jdk7"))
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
}

kotlin {
    jvmToolchain(21)
}
