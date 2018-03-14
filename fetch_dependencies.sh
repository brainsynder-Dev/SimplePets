#!/bin/bash
rm libs/craftbukkit-*
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.8.8-R0.1-SNAPSHOT.jar -P libs/
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.9-R0.1-SNAPSHOT.jar -P libs/
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.9.4-R0.1-SNAPSHOT.jar -P libs/
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.10.2-R0.1-SNAPSHOT.jar -P libs/
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.11.2-R0.1-SNAPSHOT.jar -P libs/
wget https://yivesmirror.com/files/craftbukkit/craftbukkit-1.12.2-R0.1-SNAPSHOT-b1479.jar -P libs/
mv libs/craftbukkit-1.12.2-R0.1-SNAPSHOT-b1479.jar libs/craftbukkit-1.12.2-R0.1-SNAPSHOT.jar
