import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "org.bandev"
version = "2.0.1"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    // Kord Extensions - https://github.com/Kord-Extensions/kord-extensions
    implementation(libs.kordextensions.kordextensions)
    implementation(libs.kordextensions.unsafe)

    // KMongo Coroutine - https://github.com/Litote/kmongo
    implementation(libs.kmongo.coroutine)

    // Hybrid commands - https://github.com/qbosst/kordex-hybrid-commands
    implementation(libs.kordextensions.hybrid)

    // KordX.Emoji - https://github.com/kordlib/kordx.emoji
    implementation (libs.kordx.emoji)

    // SLF4J (Required by Kord) - https://github.com/qos-ch/slf4j
    implementation(libs.slf4j.simple)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.create("stage") {
    dependsOn("installDist")
}

application {
    mainClass.set("MainKt")
}