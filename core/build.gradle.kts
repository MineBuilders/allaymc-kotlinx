plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

dependencies {
    // use shared libs
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("stdlib-jdk7"))
    compileOnly(kotlin("stdlib-jdk8"))
    compileOnly(kotlin("reflect"))
    compileOnly(libs.kotlinx.coroutines.core)
    compileOnly(libs.kotlinx.coroutines.swing)
}

publishing {
    publications {
        create<MavenPublication>("core") {
            from(components["java"])
            groupId = "com.github.MineBuilders.allaymc-kotlinx"
            artifactId = "core"
            version = project.version.toString()
        }
    }
}
