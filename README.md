<div align="center">
  <h1 style="text-decoration: underline">SimplePets</h1>
  <a href="https://repo.bsdevelopment.org/#/releases/org/bsdevelopment/simplepets/api"><img src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo.bsdevelopment.org%2Freleases%2Forg%2Fbsdevelopment%2Fsimplepets%2Fapi%2Fmaven-metadata.xml&style=for-the-badge&label=Current%20API%20Version&color=red"></a> 
  <a href="https://www.codefactor.io/repository/github/brainsynder-dev/simplepets"><img src="https://img.shields.io/codefactor/grade/github/brainsynder-dev/simplepets?style=for-the-badge&label=Codefactor%20Grade" alt="CodeFactor" /></a> 
  <a href="https://jenkins.bsdevelopment.org/job/SimplePets/"><img src="https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fjenkins.bsdevelopment.org%2Fjob%2FSimplePets%2F&style=for-the-badge&label=Jenkins%20Status"></a></br>
  <img src="https://i.imgur.com/EUDSE8P.png" alt="SimplePets Logo" height="600"/>
</div>

## Requirements:
- Spigot Version 1.21.6 - 1.21.11
- Java 25 (If on 26.1 and up)
- Java 21 (If on 1.20.5 and 1.21.11)
- Java 17 (If on 1.19 -> 1.20.4)

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
    <version>R5-B310</version>  <!-- This version is automatically updated -->
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
    implementation 'org.bsdevelopment.simplepets:api:R5-B310' // This version is automatically updated
}
```

Gradle Dependency (Kotlin DSL):
```kotlin
repositories {
    maven("https://repo.bsdevelopment.org/releases")
}

dependencies {
    implementation("org.bsdevelopment.simplepets:api:R5-B310") // This version is automatically updated
}
```

---

## How to compile yourself:
#### Notice as of `Febuary 18th 2026`
With the release of `R5-B292` we have moved to a gradle project and there are no more jars for each version.<br>
Instead, there is now only a single SimplePets.jar file located in the `build/libs` folder.<br>
If you wish to compile your own version of the plugin you can do so by running the following command: `gradle clean build`

~~When compiling a custom version you need to supply a 'revision' variable~~  
~~which will be your custom version. If no revision is supplied the version~~  
~~will default to be `5.0-BUILD-0`~~  
~~**Example:** `-Drevision=5.0-BUILD-100`~~

~~There are a few different ways you can compile the plugin (as of `May 1st 2024`):~~ 
~~- If you want to compile all current supported version you can run this command: `mvn clean install -Drevision={version}`~~
~~- If you want to compile a specific supported version run a command similar to this: `mvn clean install -Drevision={version} -Dtarget-mc=1.20.6`~~
~~- If you want to compile the latest supported version run this command: `mvn clean install -Drevision={version} -Platest`~~
