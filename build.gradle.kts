plugins {
    id("net.neoforged.moddev") version "2.0.138"
    id("com.almostreliable.almostgradle") version "2.1.1"
}

repositories {
    mavenCentral()
    maven("https://maven.latvian.dev/releases")
    maven("https://www.cursemaven.com")
    maven {
        setUrl("https://jitpack.io")
        content {
            includeGroup("com.github.rtyley")
        }
    }
}

almostgradle.setup {
    tests {
        testMod = true
    }
}

neoForge {
    runs {
        named("testmod") {
            val exampleScripts = project.rootDir.resolve("example_scripts").toString()
            systemProperty(almostgradle.modId + ".example_scripts", exampleScripts)
        }
    }
}
dependencies {
    val kubejsVersion: String by project
    implementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")
    testImplementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")
    implementation("dev.latvian.mods:better-advanced-tooltips:2601.1.0-build.9")
    testImplementation("dev.latvian.mods:better-advanced-tooltips:2601.1.0-build.9")
}

tasks.test {
    failOnNoDiscoveredTests = false
}
