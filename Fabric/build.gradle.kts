import net.fabricmc.loom.task.RemapJarTask

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val fabricApiVersion: String by project
val kubejsVersion: String by project

plugins {
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    if (project.findProperty("enableAccessWidener") == "true") { // Optional property for `gradle.properties` to enable access wideners.
        accessWidenerPath.set(project(":Common").loom.accessWidenerPath)
        println("Access widener enabled for project ${project.name}. Access widener path: ${loom.accessWidenerPath.get()}")
    }
}

val common by configurations
val shadowCommon by configurations
dependencies {
    // loader
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modApi("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion+$minecraftVersion")

    modApi("dev.latvian.mods:kubejs-fabric:${kubejsVersion}")

    modImplementation("curse.maven:probejs-585406:4750395")

    // common module
    common(project(":Common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":Common", "transformProductionFabric")) { isTransitive = false }
}

tasks {
    // allow discovery of AWs from dependencies
    named<RemapJarTask>("remapJar") {
        injectAccessWidener.set(true)
    }
}

/**
 * force the fabric loader and api versions that are defined in the project
 * some mods ship another version which crashes the runtime
 */
configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:$fabricLoaderVersion")
        force("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion+$minecraftVersion")
    }
}
