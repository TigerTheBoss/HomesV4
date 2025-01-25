group = "org.slimecraft"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id ("com.gradleup.shadow") version "9.0.0-beta4"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.zaxxer:HikariCP:6.2.1")
    compileOnly("org.incendo:cloud-paper:2.0.0-beta.10")
    compileOnly("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.slimecraft:bedrock:1.0-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }
}