plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
}

dependencies {
    // use shared libs
    compileOnly(projects.plugin.stdlib)
}

publishing {
    publications {
        create<MavenPublication>("core") {
            from(components["java"])
            groupId = "com.github.MineBuilders"
            artifactId = "allaymc-kotlinx"
            version = project.version.toString()
        }
    }
}
