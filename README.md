<div align="center">
  <img src="https://i.imgur.com/hXGO1g0.png" alt="SimplePets" width="720"/><br>
  <a href="https://repo.bsdevelopment.org/#/releases/org/bsdevelopment/simplepets/api"><img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.bsdevelopment.org%2Freleases%2Forg%2Fbsdevelopment%2Fsimplepets%2Fapi%2Fmaven-metadata.xml&style=for-the-badge&label=Current%20API%20Version&color=red"></a> 
  <a href="https://www.codefactor.io/repository/github/brainsynder-dev/simplepets"><img src="https://img.shields.io/codefactor/grade/github/brainsynder-dev/simplepets?style=for-the-badge&label=Codefactor%20Grade" alt="CodeFactor" /></a> 
  <a href="https://jenkins.bsdevelopment.org/job/SimplePets/"><img src="https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.bsdevelopment.org%2Fjob%2FSimplePets%2F&style=for-the-badge&label=Jenkins%20Status"></a></br>
</div>

---

## Requirements
| Minecraft            | Java Version | Notes                            |
|----------------------|--------------|----------------------------------|
| `26.2`               | Java 25      | Latest                           |
| `26.1`               | Java 25      |                                  |
| `1.21.11`            | Java 21      |                                  |
| `1.21.10`            | Java 21      |                                  |
| `1.21.8`             | Java 21      | Minimum supported version        |

Spigot or any of its forks will work.

<details>
  <summary><b>How patch versions are supported (26.1.1, 26.1.2, 26.2.1, etc.)</b></summary>

  Minecraft patch releases (Eg: `26.1.1`, `26.1.2`, `26.2.1`) usually do not change
  anything the plugin needs, so they do NOT require their own build of SimplePets.

  When the plugin starts up it will:
  1) Look for support that matches your exact server version
  2) If there is none, it will link to the newest support that shares the same
     major/minor version and is not newer than your server
     - `26.1`, `26.1.1`, and `26.1.2` will all use the `26.1` support
     - `26.2` and `26.2.1` will both use the `26.2` support
  3) You will see a message in the console letting you know your version was linked

  If a patch release DOES break something, dedicated support for that exact patch
  version is added and the plugin will automatically pick it over the linked one.

  `* Version linking only applies to MC 26 and newer, 1.21.x versions require exact support *`
</details>

<details>
  <summary><b>Older Minecraft versions (Legacy Jars)</b></summary>

  Support for older versions has been dropped over time, if you are on one of these
  versions you will need to grab the last build that supported it:
  - MC 1.21.6 - Last supported build was [`R5-B295`](https://jenkins.bsdevelopment.org/job/SimplePets/295/)
  - MC 1.21.7 - Last supported build was [`R5-B303`](https://jenkins.bsdevelopment.org/job/SimplePets/303/)
</details>

---

# API
<div align="center">
    <img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.bsdevelopment.org%2Freleases%2Forg%2Fbsdevelopment%2Fsimplepets%2Fapi%2Fmaven-metadata.xml&style=for-the-badge&label=Current%20API%20Version&color=red"><br>
</div>

Maven Dependency:
```xml
<repository>
    <id>bs-repo-releases</id>
    <url>https://repo.bsdevelopment.org/releases/</url>
</repository>

<dependency>
    <groupId>org.bsdevelopment.simplepets</groupId>
    <artifactId>api</artifactId>
    <version>R5-B314</version>  <!-- This version is automatically updated -->
</dependency>
```

Gradle Dependency (Groovy DSL):
```groovy
repositories {
    maven {
        url 'https://repo.bsdevelopment.org/releases'
    }
}

dependencies {
    implementation 'org.bsdevelopment.simplepets:api:R5-B314' // This version is automatically updated
}
```

Gradle Dependency (Kotlin DSL):
```kotlin
repositories {
    maven("https://repo.bsdevelopment.org/releases")
}

dependencies {
    implementation("org.bsdevelopment.simplepets:api:R5-B314") // This version is automatically updated
}
```

---

## How to compile yourself
#### Notice as of `Febuary 18th 2026`
With the release of `R5-B292` we have moved to a gradle project and there are no more jars for each version.<br>
Instead, there is now only a single SimplePets.jar file located in the `build/libs` folder.<br>
If you wish to compile your own version of the plugin you can do so by running the following command: `gradle clean build`

<details>
  <summary><b>Outdated Maven instructions</b></summary>

  ~~When compiling a custom version you need to supply a 'revision' variable~~  
  ~~which will be your custom version. If no revision is supplied the version~~  
  ~~will default to be `5.0-BUILD-0`~~  
  ~~**Example:** `-Drevision=5.0-BUILD-100`~~

  ~~There are a few different ways you can compile the plugin (as of `May 1st 2024`):~~ 
  ~~- If you want to compile all current supported version you can run this command: `mvn clean install -Drevision={version}`~~
  ~~- If you want to compile a specific supported version run a command similar to this: `mvn clean install -Drevision={version} -Dtarget-mc=1.20.6`~~
  ~~- If you want to compile the latest supported version run this command: `mvn clean install -Drevision={version} -Platest`~~
</details>
