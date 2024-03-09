import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun environment(key: String) = providers.environmentVariable(key)

plugins {
    // Java support
    id("java")
    // Kotlin support
//    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    // Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "1.17.2"
    // Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "2.2.0"
}

dependencies {
    implementation("org.springframework:spring-expression:6.1.4")
    implementation("org.reflections:reflections:0.10.2") {
        exclude(group = "org.slf4j")
    }
    testCompileOnly("com.alibaba:fastjson:1.2.83")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.3")
    // https://projectlombok.org/setup/gradle
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

group = "ink.organics"
version = "2.0.5"


repositories {
    mavenCentral()
}

intellij {
    // 这个版本是本地Runtime版本
    version.set("2023.3")
    updateSinceUntilBuild.set(false)
    // https://github.com/JetBrains/gradle-intellij-plugin/issues/38
    plugins.set(
        listOf(
            "org.intellij.intelliLang",
            "com.intellij.java",
            "org.jetbrains.kotlin",
            "com.intellij.properties"
        )
    )
}

tasks {

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    wrapper {
        gradleVersion = "8.6"
    }

    buildSearchableOptions {
        enabled = false
    }

    instrumentCode {
        enabled = false
    }

    instrumentTestCode {
        enabled = false
    }

    test {
        // 这里要签出一个完整的 Intellij IC 作为JVM语言的测试环境，并且要注意版本与 version.set("2022.3") 分发环境相同 。这个配置真蠢。
        // https://plugins.jetbrains.com/docs/intellij/testing-faq.html#how-to-test-a-jvm-language
        systemProperty("idea.home.path", "D:\\IdeaProjects\\intellij-community")
//        systemProperty("idea.home.path", project.projectDir)
        println(getSystemProperties())
    }

    patchPluginXml {
        // 这个版本是插件页面展示的最低支持版本
        sinceBuild.set("223")

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
        changeNotes.set(provider { changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML) })
    }

    register("printChangelog") {
        println(changelog.renderItem(changelog.getLatest(), Changelog.OutputType.MARKDOWN))
    }

    signPlugin {
        certificateChain.set(environment("CERTIFICATE_CHAIN"))
        privateKey.set(environment("PRIVATE_KEY"))
        password.set(environment("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(environment("PUBLISH_TOKEN"))
    }
}



