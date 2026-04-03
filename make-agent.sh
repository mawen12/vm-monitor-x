#!/bin/bash

set -euo pipefail

DIR=`pwd`
MF="$DIR/agent/src/main/resources/MANIFEST.MF"
SRC="$DIR/agent/src/main/java"
MAIN="$SRC/com/github/mawen12/Bootstrap.java"
JAR=vm-monitor-x.jar

echo "Compiling java agent from $SRC"
rm -rf ./build/classes/
rm -rf ./backend/libs/

mkdir -p ./build/classes/
mkdir -p ./backend/libs/

javac -source 8 -target 8 -cp ${SRC} -d build/classes ${MAIN}

cd ./build/classes
echo "Adding manifest $MF"

jar -cvfm ${JAR} ${MF} com

mv ${JAR} ../../backend/libs/

cd ${DIR}
echo "Created agent jar"

rm -rf ./build/classes/
ls -l ./backend/libs/ | grep $JAR
echo "Done"