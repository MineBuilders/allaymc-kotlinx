plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradleup.shadow)
}

dependencies {
    compileOnly(libs.allaymc.api)
    compileOnly(projects.plugin)
}

kotlin {
    jvmToolchain(21)
}
