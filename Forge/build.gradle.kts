val minecraftVersion: String by project
val forgeVersion: String by project
val modId: String by project
val kubejsVersion: String by project

plugins {
    id("com.github.johnrengelman.shadow") version ("8.1.1")
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    if (project.findProperty("enableAccessWidener") == "true") { // Optional property for `gradle.properties` to enable access wideners.
        accessWidenerPath.set(project(":Common").loom.accessWidenerPath)
        forge {
            convertAccessWideners.set(true)
            extraAccessWideners.add(loom.accessWidenerPath.get().asFile.name)
        }
        println("Access widener enabled for project ${project.name}. Access widener path: ${loom.accessWidenerPath.get()}")
    }

    forge {
        mixinConfigs("$modId-common.mixins.json" , "$modId-forge.mixins.json")
    }
}

val common by configurations
val shadowCommon by configurations
dependencies {
    // loader
    forge("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")


    modApi("dev.latvian.mods:kubejs-forge:${kubejsVersion}")
    localRuntime("io.github.llamalad7:mixinextras-forge:0.2.0-rc.4")

    modImplementation("curse.maven:probejs-585406:4750396")

    // common module
    common(project(":Common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":Common", "transformProductionForge")) { isTransitive = false }
}
