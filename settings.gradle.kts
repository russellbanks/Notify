rootProject.name = "KtNotify"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            kordEx()
            KMongo()
            kordExHybrid()
            kordXEmoji()
            slf4j()
        }
    }
}

fun VersionCatalogBuilder.kordEx() {
    val kordEx = version("kordEx", "1.5.2-RC1")

    library("kordextensions-kordextensions","com.kotlindiscord.kord.extensions", "kord-extensions").versionRef(kordEx)
    library("kordextensions-unsafe","com.kotlindiscord.kord.extensions", "unsafe").versionRef(kordEx)
}

fun VersionCatalogBuilder.KMongo() {
    library("kmongo-coroutine","org.litote.kmongo", "kmongo-coroutine").version("4.5.1")
}

fun VersionCatalogBuilder.kordExHybrid() {
    library("kordextensions-hybrid","io.github.qbosst", "kordex-hybrid-commands").version("1.0.4-SNAPSHOT")
}

fun VersionCatalogBuilder.kordXEmoji() {
    library("kordx-emoji","dev.kord.x", "emoji").version("0.5.0")
}

fun VersionCatalogBuilder.slf4j() {
    library("slf4j-simple", "org.slf4j", "slf4j-simple").version("1.7.36")
}
