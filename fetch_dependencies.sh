#!/bin/bash
rm libs/craftbukkit-*

wget https://archive.mcmirror.io/CraftBukkit/craftbukkit-1.11.2-R0.1-SNAPSHOT.jar -P libs/
wget https://pluginwiki.us/mcmirror/archive/craftbukkit-1.15-R0.1-SNAPSHOT.jar -P libs/

wget https://archive.mcmirror.io/Spigot/spigot-1.12.2-R0.1-SNAPSHOT-b1648.jar -P libs/
mv libs/spigot-1.12.2-R0.1-SNAPSHOT-b1648.jar libs/craftbukkit-1.12.2-R0.1-SNAPSHOT.jar

wget https://archive.mcmirror.io/Spigot/spigot-1.13-R0.1-SNAPSHOT-b1850.jar -P libs/
mv libs/spigot-1.13-R0.1-SNAPSHOT-b1850.jar libs/craftbukkit-1.13-R0.1-SNAPSHOT.jar

wget https://archive.mcmirror.io/Spigot/spigot-1.13.2-R0.1-SNAPSHOT-b2147.jar -P libs/
mv libs/spigot-1.13.2-R0.1-SNAPSHOT-b2147.jar libs/craftbukkit-1.13.1-R0.1-SNAPSHOT.jar

# wget https://mcmirror.io/files/Spigot/Spigot-1.14-0c02b0c-20190425-0538.jar -P libs/
# mv libs/Spigot-1.14-0c02b0c-20190425-0538.jar libs/craftbukkit-1.14-R0.1-SNAPSHOT.jar

wget https://mcmirror.io/files/Spigot/Spigot-1.14.4-ea7e48b-20190808-1048.jar -P libs/
mv libs/Spigot-1.14.4-ea7e48b-20190808-1048.jar libs/craftbukkit-1.14-R0.1-SNAPSHOT.jar