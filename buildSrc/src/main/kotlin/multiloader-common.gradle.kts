plugins {
    id("java")
    id("idea")
    id("java-library")
}

version = "${loader}-${commonMod.version}+mc${stonecutterBuild.current.version}"

base {
    archivesName = commonMod.id
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(commonProject.prop("java.version")!!)
    // withSourcesJar()
    // withJavadocJar()
}

repositories {
    mavenLocal()
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven("https://repo.spongepowered.org/repository/maven-public") { name = "Sponge" }
        }
        filter { includeGroupAndSubgroups("org.spongepowered") }
    }
    exclusiveContent {
        forRepositories(
            maven("https://maven.parchmentmc.org") { name = "ParchmentMC" },
            maven("https://maven.neoforged.net/releases") { name = "NeoForge" },
            maven("https://maven.minecraftforge.net/") { name = "MinecraftForge" }
        )
        filter { includeGroup("org.parchmentmc.data") }
    }

    maven("https://maven.clwn.org/releases") { name = "ClownsProd Releases" }
    maven("https://maven.clwn.org/snapshots") { name = "ClownsProd Snapshots" }

    maven("https://www.cursemaven.com")
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.terraformersmc.com/releases/") { name = "TerraformersMC" }
    maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
    maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    maven("https://thedarkcolour.github.io/KotlinForForge/")

    maven("https://jitpack.io")

    maven("https://maven.architectury.dev/")
}

tasks {

    processResources {
        val expandProps = mapOf(
            "java_version" to commonMod.propOrNull("java.version"),
            "mod_id" to commonMod.id,
            "mod_name" to commonMod.name,
            "mod_version" to commonMod.version,
            "mod_group" to commonMod.group,
            "mod_author" to commonMod.author,
            "mod_description" to commonMod.description,
            "mod_license" to commonMod.license,
            "mod_github" to commonMod.github,
            "minecraft_version" to commonMod.propOrNull("minecraft_version"),
            "min_minecraft_version" to commonMod.propOrNull("min_minecraft_version"),
            "fabric_loader_version" to commonMod.depOrNull("fabric_loader"),
            "fabric_api_version" to commonMod.depOrNull("fabric-api"),
            "neoforge_version" to commonMod.depOrNull("neoforge"),
            "mod_menu_version" to commonMod.depOrNull("modmenu")
        ).filterValues { it?.isNotEmpty() == true }.mapValues { (_, v) -> v!! }

        val jsonExpandProps = expandProps.mapValues { (_, v) -> v.replace("\n", "\\\\n") }

        filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml")) {
            expand(expandProps)
        }

        filesMatching(listOf("pack.mcmeta", "fabric.mod.json")) {
            expand(jsonExpandProps)
        }

        filesMatching(listOf("interactiveguns.mixins.json", "interactiveguns.fabric.mixins.json", "interactiveguns.neoforge.mixins.json")) {
            expand(jsonExpandProps)
        }

        inputs.properties(expandProps)
    }
}

tasks.named("processResources") {
    dependsOn(":common:${commonMod.propOrNull("minecraft_version")}:stonecutterGenerate")
}
