@file:Suppress("UnstableApiUsage")

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
    id("net.neoforged.moddev") version "0.1.112"
    id("com.github.gmazzo.buildconfig") version "4.0.4"
}

repositories {
    maven("https://maven.saps.dev/minecraft")
    maven("https://www.cursemaven.com")
}

base {
    archivesName.set("$modId-neoforge")
    version = "$minecraftVersion-$modVersion"
}

neoForge {
    version = neoforgeVersion

    addModdingDependenciesTo(sourceSets.test.get())

    mods {
        create(modId) {
            sourceSets.add(sourceSets.main.get())
        }

        create("testmod") {
            sourceSets.add(sourceSets.test.get())
        }
    }

    runs {
        val exampleScripts = project.rootDir.resolve("example_scripts").toString()
        configureEach {
            jvmArgument("-XX:+IgnoreUnrecognizedVMOptions")
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
        }
        create("gametest") {
            server();
            sourceSet.set(sourceSets.test.get())
            systemProperty("neoforge.gameTestServer", "true")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            systemProperty("lootjs.example_scripts", exampleScripts)
        }
        create("testmod") {
            client();
            sourceSet.set(sourceSets.test.get())
            systemProperty("neoforge.gameTestServer", "true")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
            systemProperty("lootjs.example_scripts", exampleScripts)
        }
        create("client") {
            client()
        }
        create("server") {
            server()
        }
    }
}

dependencies {
    implementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")
    testImplementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")

    implementation("com.google.code.findbugs:jsr305:3.0.2")
}

tasks {
    /**
     * Resource processing for defined targets. This will replace `${key}` with the specified values from the map below.
     */
    processResources {
        val resourceTargets = listOf("META-INF/neoforge.mods.toml", "pack.mcmeta")

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
}

extensions.configure<JavaPluginExtension> {
    withSourcesJar()
}

buildConfig {
    buildConfigField("String", "MOD_ID", "\"$modId\"")
    buildConfigField("String", "MOD_NAME", "\"$modName\"")
    buildConfigField("String", "MOD_VERSION", "\"$version\"")
    packageName(modPackage)
    useJavaOutput()
}
