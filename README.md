# SimplePets
<div align="center">
  <img src="https://img.shields.io/maven-metadata/v?color=red&label=Current%20Version&metadataUrl=https%3A%2F%2Frepo.bsdevelopment.org%2Freleases%2Fsimplepets%2Fbrainsynder%2FAPI%2Fmaven-metadata.xml"></br>
  <a href="https://www.codefactor.io/repository/github/brainsynder-dev/simplepets"><img src="https://www.codefactor.io/repository/github/brainsynder-dev/simplepets/badge" alt="CodeFactor" /></a> 
  <img src="https://ci.bsdevelopment.org/job/SimplePets_v5/badge/icon?subject=v5%20Recode (1.19 -> LATEST)"></br>
  <img src="https://i.imgur.com/EUDSE8P.png" alt="SimplePets Logo" height="500">
</div>

## Requirements:
- Spigot Version 1.19 - 1.20.4
- Java 17 (If on 1.19 and up)

## API
<div align="center">
    <img src="https://img.shields.io/maven-metadata/v?color=red&label=Current%20Version&metadataUrl=https%3A%2F%2Frepo.bsdevelopment.org%2Freleases%2Fsimplepets%2Fbrainsynder%2FAPI%2Fmaven-metadata.xml&style=for-the-badge"><br>
</div>

```xml
<repository>
    <id>bs-repo-releases</id>
    <url>https://repo.bsdevelopment.org/releases</url>
</repository>

<dependency>
    <groupId>simplepets.brainsynder</groupId>
    <artifactId>API</artifactId>
    <version>{LATEST VERSION}</version>
</dependency>
```

## How to compile yourself:
Placeholders:
- {build} = The build number you want as the version (e.g 1000 = 5.0-BUILD-1000)
- {job} = This can be set to what you want its mostly used by the update checker (e.g SimplePets_v5)

This is the maven command we use to compile the plugin: 
`mvn clean install -f pom.xml -Denv.BUILD_NUMBER={build} -Denv.JOB_NAME={job}`

## Files Used:
- Spigot API
- Spigot [remapped-mojang] (Used for all NMS code)
