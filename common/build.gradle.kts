plugins {
    id("multiloader-common")
    id("fabric-loom") version "1.14-SNAPSHOT"
    kotlin("jvm") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.14"
}

stonecutter {

}

fletchingTable {
    j52j.register("main") {
        extension("json", "**/*.json5")
    }
}

loom {
    mixin {
        useLegacyMixinAp = false
    }
}

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = commonMod.mc)
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${commonMod.mc}:$parchmentVersion@zip")
        }
    })

    compileOnly("org.spongepowered:mixin:0.8.5")
    modCompileOnly("net.fabricmc:fabric-loader:${commonMod.dep("fabric_loader")}")

    modApi("net.xmx.velthoric:velthoric-fabric:${commonMod.dep("velthoric")}")

    modApi("net.timtaran.interactivemc:interactivemc-fabric:${commonMod.dep("interactivemc")}")

    modImplementation(commonMod.modrinth("vivecraft", "${commonMod.dep("vivecraft")}-fabric"))
    runtimeOnly("org.lwjgl:lwjgl-openvr:${commonMod.dep("lwjgl")}")
    runtimeOnly("org.lwjgl:lwjgl-openvr:${commonMod.dep("lwjgl")}:natives-windows")
    runtimeOnly("org.lwjgl:lwjgl-openvr:${commonMod.dep("lwjgl")}:natives-linux")
    runtimeOnly("org.lwjgl:lwjgl-openvr:${commonMod.dep("lwjgl")}:natives-macos")

    runtimeOnly("com.illposed.osc:javaosc-core:0.9")
    runtimeOnly("com.github.bhaptics:tact-java:0.1.4")
    runtimeOnly("org.java-websocket:Java-WebSocket:1.5.1")
    runtimeOnly("com.electronwill.night-config:toml:3.6.6")
    runtimeOnly("com.electronwill.night-config:core:3.6.6")
}

val commonJava: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

artifacts {
    afterEvaluate {
        val mainSourceSet = sourceSets.main.get()
        mainSourceSet.java.sourceDirectories.files.forEach {
            add(commonJava.name, it)
        }
        mainSourceSet.resources.sourceDirectories.files.forEach {
            add(commonResources.name, it)
        }
    }
}