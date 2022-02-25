import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "org.bandev"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.kotlindiscord.com/repository/maven-public/")
}

dependencies {
    // Kord - https://github.com/kordlib/kord
    implementation("dev.kord:kord-core:0.8.0-M10")

    // Kord Extensions - https://github.com/Kord-Extensions/kord-extensions
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:1.5.2-RC1")

    // KMongo Coroutine - https://github.com/Litote/kmongo
    implementation("org.litote.kmongo:kmongo-coroutine:4.5.0")

    // Hybrid commands - https://github.com/qbosst/kordex-hybrid-commands
    implementation("io.github.qbosst:kordex-hybrid-commands:1.0.4-SNAPSHOT")

    // KordX.Emoji - https://github.com/kordlib/kordx.emoji
    implementation ("dev.kord.x:emoji:0.5.0")

    // SLF4J (Required by Kord) - https://github.com/qos-ch/slf4j
    implementation("org.slf4j:slf4j-simple:1.7.36")
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