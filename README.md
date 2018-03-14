# SimplePets
master branch: ![Build Status](https://travis-ci.org/brainsynder-Dev/SimplePets.svg?branch=master "Build Status")
development branch: ![Build Status](https://travis-ci.org/brainsynder-Dev/SimplePets.svg?branch=development "Build Status")
![SimplePets Logo](http://brainsynder.us/assets/SimplePets.jpg "SimplePets Logo")

## Requirements:
- SimpleAPI (Version: 3.7+)the plugin will not function without the plugin.
- Spigot Version 1.8.8, 1.9, 1.9.4, 1.10, 1.11, 1.11.2, or 1.12 (Recommended)
- Java 8 (If you are not on Java 8 then update.... like really...)

## NOTICE:
Just a little notice I will be removing 1.8 -> 1.10 support due to it being a huge pain to make the plugin compatible with the outdated versions. I feel that me supporting those versions are holding SimplePets back in terms of feature wise. 

## Files Used:
- Spigot API (inside pom.xml)
- SimpleAPI (v3.7) (libs folder)
- plotsquared-api:latest (inside pom.xml)
- craftbukkit-1.8.8-R0.1-SNAPSHOT.jar
- craftbukkit-1.9-R0.1-SNAPSHOT.jar
- craftbukkit-1.9.4-R0.1-SNAPSHOT.jar
- craftbukkit-1.10.2-R0.1-SNAPSHOT.jar
- craftbukkit-1.11.2-R0.1-SNAPSHOT.jar
- craftbukkit-1.12.2-R0.1-SNAPSHOT.jar

## Compile

First you need to save the above mentioned craftbukkit versions into the `libs` folder. On Linux this can be done automatically with the `fetch_dependencies.sh` script. On Windows you need to go to Yivesmirror and download them manually as no bat script has been written yet. Make sure the files are correctly named as written above.

Once the libraries are in place you can compile the project either directly from the command line or via your IDE.
For instructions how to do it with IntelliJ or Eclipse, please search for "Inteillj Maven tutorial". Make sure to open the project as a Maven project and not as a default Java project. Alternatively you can compile it via commandline with `mvn package`. This will produce the finished jar file at `target/SimplePets.jar`.
