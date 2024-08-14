import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    // Java support
    id("java")
    // Kotlin support
//    id("org.jetbrains.kotlin.jvm") version "1.9.10"
    // Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij.platform") version "2.0.1"
    // Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "2.2.0"
}

group = "ink.organics"
version = "2.0.6"

// Configure project's dependencies
repositories {
    mavenCentral()
    // IntelliJ Platform Gradle Plugin Repositories Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-repositories-extension.html
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {

    intellijPlatform {
        intellijIdeaCommunity("2023.3")

        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.properties")
        bundledPlugin("org.jetbrains.kotlin")

        pluginVerifier()
        zipSigner()
        instrumentationTools()

        testFramework(TestFrameworkType.Plugin.Java)
    }

//    intellijPlatformTesting {
//        runIde
//        testIde
//    }

    implementation("org.springframework:spring-expression:6.1.4")
    implementation("org.reflections:reflections:0.10.2") {
        exclude(group = "org.slf4j")
    }

    testCompileOnly("junit:junit:4.13.2")
    testCompileOnly("com.alibaba:fastjson:1.2.83")
    testCompileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.3")
    // https://projectlombok.org/setup/gradle
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        // Get the latest available change notes from the changelog file
        changeNotes = changelog.renderItem(changelog.getLatest(), Changelog.OutputType.HTML)

        // 这个版本是插件页面展示的最低支持版本
        ideaVersion {
            sinceBuild = "233"
        }
    }

    signing {
        certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PRIVATE_KEY")
        password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {

//    printBundledPlugins.get().printBundledPlugins()

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
        options.encoding = "UTF-8"
    }

    wrapper {
        gradleVersion = "8.6"
    }

//    instrumentCode {
//        enabled = false
//    }
//
//    instrumentTestCode {
//        enabled = false
//    }

    test {
        // 这里要签出一个完整的 Intellij IC 作为JVM语言的测试环境，并且要注意版本与 version.set("2022.3") 分发环境相同 。这个配置真蠢。
        // https://plugins.jetbrains.com/docs/intellij/testing-faq.html#how-to-test-a-jvm-language
        systemProperty("idea.home.path", "D:\\IdeaProjects\\intellij-community")
//        systemProperty("idea.home.path", project.projectDir)
        println(getSystemProperties())
    }

    register("printChangelog") {
        println(changelog.renderItem(changelog.getLatest(), Changelog.OutputType.MARKDOWN))
    }
}



