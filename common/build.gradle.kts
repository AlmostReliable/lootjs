val enabledPlatforms: String by project
val fabricLoaderVersion: String by project
val modId: String by project
val modName: String by project
val modPackage: String by project
val kubejsVersion: String by project

plugins {
    id("com.github.gmazzo.buildconfig") version ("4.0.4")
}

architectury {
    common(enabledPlatforms.split(","))
}

loom {
    if (project.findProperty("enableAccessWidener") == "true") { // Optional property for `gradle.properties` to enable access wideners.
        accessWidenerPath.set(file("src/main/resources/$modId.accesswidener"))
        println("Access widener enabled for project ${project.name}. Access widener path: ${loom.accessWidenerPath.get()}")
    }
}

dependencies {
    // loader
    // required here for the @Environment annotations and the mixin dependencies
    // Do NOT use other classes from the Fabric loader!
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")


    modApi("dev.latvian.mods:kubejs:${kubejsVersion}")
}

buildConfig {
    buildConfigField("String", "MOD_ID", "\"$modId\"")
    buildConfigField("String", "MOD_NAME", "\"$modName\"")
    buildConfigField("String", "MOD_VERSION", "\"$version\"")
    packageName(modPackage)
    useJavaOutput()
}
