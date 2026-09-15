import org.bsdevelopment.spigotweight.extension.UserdevExtension

plugins {
    id("org.bsdevelopment.java-conventions")
    alias(libs.plugins.spigotweight)
    id("com.gradleup.shadow")
}

var mcVersion = "26.3"

var nmsVersion = "v$mcVersion".replace(".", "_")
var latestMinecraft = "$mcVersion-R0.1-SNAPSHOT"

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":main"))
    implementation(project(":versions"))

    compileOnly(libs.pluginutils)
}

spigotweight {
    minecraftVersion = mcVersion
    target = UserdevExtension.SpigotAPITarget.SPIGOT   // SPIGOT or PAPER
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks {
    shadowJar {
        archiveBaseName.set("SimplePets")
        archiveVersion.set(mcVersion)
        archiveClassifier.set("")

        var groupID = "simplepets.brainsynder"
        relocate("$groupID.nms", "$groupID.versions.$nmsVersion")
    }
}
