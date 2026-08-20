pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.bsdevelopment.org/releases/")
    }
}

rootProject.name = "SimplePets"
include(":api")
include(":main")

// include(":versions:v1_21_6") // Removed support in version R5-B296
// include(":versions:v1_21_7") // Removed support in version R5-B304
// include(":versions:v1_21_8") // Removed support in version R6-B1
// include(":versions:v1_21_10") // Removed support in version R6-B1
// include(":versions:v1_21_11") // Removed support in version R6-B1
//include(":versions:v26_1")
//include(":versions:v26_2")
// AUTOMATION END

project(":api").projectDir  = file("plugin-modules/api")
project(":main").projectDir = file("plugin-modules/main")
