plugins {
    alias(libs.plugins.kotlin.jvm)
    id("maven-publish")
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
