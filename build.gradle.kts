import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    application
}

group = "com.russellbanks"
version = "2.1.0"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // KMongo Coroutine - https://github.com/Litote/kmongo
    implementation(libs.kmongo.coroutine)

    // Kord Extensions - https://github.com/Kord-Extensions/kord-extensions
    implementation(libs.kordextensions.kordextensions)
    implementation(libs.kordextensions.unsafe)

    // KordX.Emoji - https://github.com/kordlib/kordx.emoji
    implementation (libs.kordx.emoji)

    // KotlinX Coroutines - https://github.com/Kotlin/kotlinx.coroutines
    implementation(libs.kotlinx.coroutines.core)

    // KotlinX DateTime - https://github.com/Kotlin/kotlinx-datetime

    // SLF4J (Required by Kord) - https://github.com/qos-ch/slf4j
    implementation(libs.slf4j.simple)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.current().toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

application {
    mainClass.set("MainKt")
}
