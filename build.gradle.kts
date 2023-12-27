@file:Suppress("UnstableApiUsage")

import net.fabricmc.loom.task.RemapJarTask

val license: String by project
val neoforgeVersion: String by project
val minecraftVersion: String by project
val modPackage: String by project
val modVersion: String by project
val modId: String by project
val modName: String by project
val modDescription: String by project
val modAuthor: String by project
val githubRepo: String by project
val githubUser: String by project
val kubejsVersion: String by project

plugins {
    java
    `maven-publish`
    id("dev.architectury.loom") version ("1.4.+")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.gmazzo.buildconfig") version "4.0.4"
}

val extraModsPrefix = "extra-mods"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://maven.parchmentmc.org") // Parchment
    maven("https://maven.saps.dev/minecraft")
    maven("https://www.cursemaven.com")
    maven("https://maven.neoforged.net/releases")
    flatDir {
        name = extraModsPrefix
        dir(file("$extraModsPrefix-$minecraftVersion"))
    }
}


base.archivesName.set(modId)
version = "$minecraftVersion-$modVersion"

loom {
    loom.silentMojangMappingsLicense()
    loom.createRemapConfigurations(sourceSets.getByName("test"))

    accessWidenerPath.set(file("src/main/resources/${modId}.accesswidener"))

    // load the test mod manually because forge always uses main by default
    mods {
        create("testmod") {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        create("test_client") {
            name("Testmod Client")
            client()
            source(sourceSets.test.get())
        }

        create("test_server") {
            name("Testmod Server")
            server()
            source(sourceSets.test.get())
        }

        create("gametest") {
            name("Gametest")
            server()
            source(sourceSets.test.get())
            property("neoforge.gameTestServer", "true")
            property("almostlib.gametest.testPackages", "testmod.*")
        }

        forEach {
            it.runDir("run")
            // Allows DCEVM hot-swapping when using the JetBrains Runtime (https://github.com/JetBrains/JetBrainsRuntime).
            it.vmArgs("-XX:+IgnoreUnrecognizedVMOptions", "-XX:+AllowEnhancedClassRedefinition")
            if (it.environment == "client") {
                it.programArgs("--width", "1920", "--height", "1080")
            }
        }
    }
}


dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())

    neoForge("net.neoforged:neoforge:${neoforgeVersion}")
    modApi("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")

    /**
     * Helps to load mods in development through an extra directory. Sadly this does not support transitive dependencies. :-(
     */
    fileTree("$extraModsPrefix-$minecraftVersion") { include("**/*.jar") }
        .forEach { f ->
            val sepIndex = f.nameWithoutExtension.lastIndexOf('-')
            if (sepIndex == -1) {
                throw IllegalArgumentException("Invalid mod name: '${f.nameWithoutExtension}'. Expected format: 'modName-version.jar'")
            }
            val mod = f.nameWithoutExtension.substring(0, sepIndex)
            val version = f.nameWithoutExtension.substring(sepIndex + 1)
            println("Extra mod ${f.nameWithoutExtension} detected.")
            "modLocalRuntime"("extra-mods:$mod:$version")
        }

    implementation("com.google.code.findbugs:jsr305:3.0.2")
}

/**
 * Maven publishing
 */
publishing {
    publications {
        register("mavenNeoForge", MavenPublication::class) {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish here.
    }
}

tasks {
    /**
     * Resource processing for defined targets. This will replace `${key}` with the specified values from the map below.
     */
    processResources {
        val resourceTargets = listOf("META-INF/mods.toml", "pack.mcmeta", "fabric.mod.json")

        val replaceProperties = mapOf(
            "version" to project.version as String,
            "license" to license,
            "modId" to modId,
            "modName" to modName,
            "minecraftVersion" to minecraftVersion,
            "modAuthor" to modAuthor,
            "modDescription" to modDescription,
            "neoforgeVersion" to neoforgeVersion,
            "githubUser" to githubUser,
            "githubRepo" to githubRepo,
            "kubejsVersion" to kubejsVersion
        )

        println("[Process Resources] Replacing properties in resources: ")
        replaceProperties.forEach { (key, value) -> println("\t -> $key = $value") }

        inputs.properties(replaceProperties)
        filesMatching(resourceTargets) {
            expand(replaceProperties)
        }
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    /**
     * When publishing to Maven Local, use a timestamp as version so projects can always
     * use the latest version without having to use a dummy version.
     */
    named<Task>("publishToMavenLocal") {
        version = "$version.${System.currentTimeMillis() / 1000}"
    }

    named<RemapJarTask>("remapJar") {
        atAccessWideners.add("${modId}.accesswidener")
    }

    named<Jar>("jar") {
        archiveClassifier.set("dev")
    }

}

extensions.configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

buildConfig {
    buildConfigField("String", "MOD_ID", "\"$modId\"")
    buildConfigField("String", "MOD_NAME", "\"$modName\"")
    buildConfigField("String", "MOD_VERSION", "\"$version\"")
    packageName(modPackage)
    useJavaOutput()
}
