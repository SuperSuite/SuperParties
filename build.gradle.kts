plugins {
    java
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.superpenguin"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://www.jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.14.0")
    compileOnly("net.kyori:adventure-platform-bukkit:4.3.0")

    implementation("com.github.SuperGlueLib:SuperTeamsAPI:84734c683f")
    implementation("com.github.SuperGlueLib:SuperGUIs:0.1.0")
    implementation("com.github.SuperGlueLib:LampWrapper:1c57b6d31b")
}

