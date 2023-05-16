import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    application
}

group = "org.bandev"
version = "2.0.1"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // Kord Extensions - https://github.com/Kord-Extensions/kord-extensions
    implementation(libs.kordextensions.kordextensions)
    implementation(libs.kordextensions.unsafe)

    // KMongo Coroutine - https://github.com/Litote/kmongo
    implementation(libs.kmongo.coroutine)

    // KordX.Emoji - https://github.com/kordlib/kordx.emoji
    implementation (libs.kordx.emoji)

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
