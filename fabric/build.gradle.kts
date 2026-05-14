plugins {
    `multiloader-loader`
    id("fabric-loom") version "1.14-SNAPSHOT"
    kotlin("jvm") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.14"
}

stonecutter {

}

dependencies {
    minecraft("com.mojang:minecraft:${commonMod.mc}")
    mappings(loom.layered {
        officialMojangMappings()
        commonMod.depOrNull("parchment")?.let { parchmentVersion ->
            parchment("org.parchmentmc.data:parchment-${commonMod.mc}:$parchmentVersion@zip")
        }
    })

    modImplementation("net.fabricmc:fabric-loader:${commonMod.dep("fabric_loader")}")
    modApi("net.fabricmc.fabric-api:fabric-api:${commonMod.dep("fabric_api")}+${commonMod.mc}")

    // Required dependencies
    modImplementation("com.terraformersmc:modmenu:${commonMod.dep("modmenu")}")

    modApi("com.github.Timtaran.Velthoric:velthoric-fabric:${commonMod.dep("velthoric")}")
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

loom {
    runs {
        getByName("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
        }
        getByName("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
        }
    }
}