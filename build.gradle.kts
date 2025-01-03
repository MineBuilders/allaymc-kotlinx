plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.gradleup.shadow) apply false
}

version = libs.versions.allaymc.kotlinx.get()

subprojects {
    afterEvaluate {

        runCatching {
            kotlin {
                sourceSets.all {  // all is jvm :P
                    dependencies {
                        @Suppress("VulnerableLibrariesLocal", "RedundantSuppression")
                        compileOnly(libs.allaymc.api)
                    }
                }
                compilerOptions.freeCompilerArgs.addAll(
                    "-Xcontext-receivers",  // https://kotlinlang.org/docs/whatsnew2020.html#phased-replacement-of-context-receivers-with-context-parameters
                )
            }
        }

        if (projectDir.resolve("src/main/resources/plugin.json").exists()) {
            val version = rootProject.libs.versions.allaymc.kotlinx.get()

            tasks.named("processResources") {
                doLast {
                    val origin = file("src/main/resources/plugin.json")
                    val processed = file("${layout.buildDirectory.get()}/resources/main/plugin.json")
                    val content = origin.readText().replace("\${version}", version)
                    processed.writeText(content)
                }
            }
        }

    }
}
