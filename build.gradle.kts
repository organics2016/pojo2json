import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.1.4"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.3.0"
}

dependencies {
    testCompileOnly("com.alibaba:fastjson:1.2.76")
}

group = "ink.organics"
version = "1.1.0"


repositories {
    mavenCentral()
}

intellij {
    version.set("2020.3")
    updateSinceUntilBuild.set(false)
    plugins.set(listOf("java"))
}

changelog {
    version.set("${project.version}")
    path.set("${project.projectDir}/CHANGELOG.md")
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    patchPluginXml {
        sinceBuild.set("203")

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
                File(projectDir, "README.md").readText().lines().run {
                    val start = "<!-- Plugin description -->"
                    val end = "<!-- Plugin description end -->"

                    if (!containsAll(listOf(start, end))) {
                        throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                    }
                    subList(indexOf(start) + 1, indexOf(end))
                }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(provider { changelog.getLatest().toHTML() })
    }

    publishPlugin {
        token.set(properties("intellijPublishToken"))
    }
}



