import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "org.bandev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.kord:kord-core:0.8.0-M9")
    implementation ("dev.kord.x:emoji:0.5.0")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("io.klogging:klogging-jvm:0.4.1")
    implementation("org.litote.kmongo:kmongo:4.4.0")
    implementation("org.mongodb:mongodb-driver-async:3.12.10")
    implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")
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