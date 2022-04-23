import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    // Java support
    id("java")
    // Kotlin support
//    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.3.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"
}

dependencies {
    testCompileOnly("com.alibaba:fastjson:1.2.76")
    testCompileOnly("com.fasterxml.jackson.core:jackson-annotations:2.11.0")
    // https://projectlombok.org/setup/gradle
    testCompileOnly("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")
}

group = "ink.organics"
version = "1.2.0"


repositories {
    mavenCentral()
}

intellij {
    version.set("2020.3")
    updateSinceUntilBuild.set(false)
    // https://github.com/JetBrains/gradle-intellij-plugin/issues/38
    plugins.set(listOf("java", "Kotlin"))
}

changelog {
    version.set("${project.version}")
    path.set("${project.projectDir}/CHANGELOG.md")
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
        options.encoding = "UTF-8"
    }

    test {
        // 这个路径下要存在mockJDK，其目录结构为 java/mockJDK-$JAVA_VERSION$
        // https://plugins.jetbrains.com/docs/intellij/testing-faq.html#how-to-test-a-jvm-language
        systemProperties(Pair("idea.home.path", project.projectDir))
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



