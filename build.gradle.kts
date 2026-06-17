plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
}

group = "uk.co.nikodem"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.06.05-26.1.2")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("io.github.togar2:MinestomPvP:2026.05.30-26.1.1")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation("net.kyori:option:1.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25)) // Minestom 26.1 has a minimum Java version of 25
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "uk.co.nikodem.Main"
        }
    }
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("") // Prevent the -all suffix on the shadowjar file.
    }
}