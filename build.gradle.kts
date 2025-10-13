plugins {
    id("net.neoforged.moddev") version "2.0.105"
    id("com.almostreliable.almostgradle") version "1.3.+"
}

repositories {
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
    testMod = true
}

dependencies {
    val kubejsVersion: String by project
    implementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")
    testImplementation("dev.latvian.mods:kubejs-neoforge:${kubejsVersion}")

    testLocalRuntime(almostgradle.recipeViewers.emi.dependency)
}
