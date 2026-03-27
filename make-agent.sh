#!/bin/bash

set -euo pipefail

DIR=`pwd`
MF="$DIR/src/main/resources/MANIFEST.MF"
SRC="$DIR/src/main/java"
MAIN="$SRC/com/github/mawen12/Bootstrap.java"
JAR=vm-monitor-x.jar

echo "Compiling java agent from $SRC"
rm -rf ./build/classes/
rm -rf ./build/libs/

mkdir -p ./build/classes/
mkdir -p ./build/libs/

javac -source 8 -target 8 -cp ${SRC} -d build/classes ${MAIN}

cd ./build/classes
echo "Adding manifest $MF"

jar -cvfm ${JAR} ${MF} com

mv ${JAR} ../libs/

cd ${DIR}
echo "Created agent jar"

ls -l ./build/libs/ | grep $JAR
echo "Done"