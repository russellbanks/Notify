import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
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

    // Kord Extensions - https://github.com/Kord-Extensions/kord-extensions
    val kordExVersion = "1.5.2-RC1"
    implementation("com.kotlindiscord.kord.extensions:kord-extensions:$kordExVersion")
    implementation("com.kotlindiscord.kord.extensions:unsafe:$kordExVersion")

    // KMongo Coroutine - https://github.com/Litote/kmongo
    implementation("org.litote.kmongo:kmongo-coroutine:4.5.1")

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