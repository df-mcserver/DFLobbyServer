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
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("net.minestom:minestom:2025.10.05-1.21.8")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("com.github.TogAr2:MinestomPvP:56a831b41c")
    implementation("net.kyori:adventure-text-minimessage:4.24.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // Minestom has a minimum Java version of 21
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