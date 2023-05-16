import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.sqldelight)
    application
}

group = "com.russellbanks"
version = "3.0.0"

repositories {
    mavenCentral()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // Kord Extensions - https://github.com/Kord-Extensions/kord-extensions
    implementation(libs.kordextensions.kordextensions)
    implementation(libs.kordextensions.unsafe)

    // KordX.Emoji - https://github.com/kordlib/kordx.emoji
    implementation (libs.kordx.emoji)

    // KotlinX Coroutines - https://github.com/Kotlin/kotlinx.coroutines
    implementation(libs.kotlinx.coroutines.core)

    // KotlinX DateTime - https://github.com/Kotlin/kotlinx-datetime
    implementation(libs.kotlinx.datetime)

    // SLF4J (Required by Kord) - https://github.com/qos-ch/slf4j
    implementation(libs.slf4j.simple)

    // SQLDelight - https://github.com/cashapp/sqldelight
    implementation(libs.sqldelight.primitive.adapters)
    implementation(libs.sqldelight.sqlite.driver)
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.russellbanks")
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.current().toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

application {
    mainClass.set("MainKt")
}
