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
}

dependencies {
    // Dotenv-Kotlin - https://github.com/cdimascio/dotenv-kotlin
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

    // Klogging - https://github.com/klogging/klogging
    implementation("io.klogging:klogging-jvm:0.4.3")

    // KMongo Coroutine - https://github.com/Litote/kmongo
    implementation("org.litote.kmongo:kmongo-coroutine:4.5.0")

    // Kord - https://github.com/kordlib/kord
    implementation("dev.kord:kord-core:voice-state-props-SNAPSHOT")

    // KordX.Emoji - https://github.com/kordlib/kordx.emoji
    implementation ("dev.kord.x:emoji:0.5.0") { exclude("dev.kord", "kord-core") }

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