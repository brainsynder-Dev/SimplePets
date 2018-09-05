#!/bin/bash
rm libs/craftbukkit-*
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.11.2-R0.1-SNAPSHOT.jar -P libs/
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.12.2-R0.1-SNAPSHOT-b1479.jar -P libs/
wget http://yivesmirror.com/files/spigot/spigot-1.13-R0.1-SNAPSHOT-b1726.jar -P libs/
wget http://yivesmirror.com/files/spigot/spigot-1.13.1-R0.1-SNAPSHOT-b1887.jar -P libs/
mv libs/craftbukkit-1.12.2-R0.1-SNAPSHOT-b1479.jar libs/craftbukkit-1.12.2-R0.1-SNAPSHOT.jar
mv libs/spigot-1.13-R0.1-SNAPSHOT-b1726.jar libs/craftbukkit-1.13-R0.1-SNAPSHOT.jar
mv libs/spigot-1.13.1-R0.1-SNAPSHOT-b1887.jar libs/craftbukkit-1.13.1-R0.1-SNAPSHOT.jar
