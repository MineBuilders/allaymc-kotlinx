rootProject.name = "allaymc-kotlinx"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        maven("https://jitpack.io/")
        maven("https://www.jitpack.io/")
        maven("https://repo.opencollab.dev/maven-releases/")
        maven("https://repo.opencollab.dev/maven-snapshots/")
        maven("https://storehouse.okaeri.eu/repository/maven-public/")
    }
}

include(":core")
include(":plugin")
include(":example")
