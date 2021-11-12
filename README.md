# SimplePets
<div align="center">
  <img src="https://pluginwiki.us/version/badge/SimplePets_v5.svg"></br>
  <img src="https://ci.pluginwiki.us/job/SimplePets/badge/icon?subject=Master (outdated)"> 
  <img src="https://ci.pluginwiki.us/job/SimplePets-Experimental/badge/icon?subject=Experimental (1.15->1.16.5)"> 
  <img src="https://ci.pluginwiki.us/job/SimplePets_v5/badge/icon?subject=v5%20Recode (1.17)"></br>
  <img src="https://i.imgur.com/60pNn41.jpg" alt="SimplePets Logo">
</div>

## Requirements:
- Spigot Version 1.17
- Java 16

## API
<div align="center">
    <img src="https://pluginwiki.us/version/badge/SimplePets_v5.svg"><br>
</div>

```xml
<repository>
    <id>bs-repo</id>
    <url>http://ci.pluginwiki.us/plugin/repository/everything/</url>
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
- Spigot API (inside pom.xml)
- Spigot [remapped-mojang] (inside pom.xml)
