#!/bin/bash

DIR=`pwd`

echo "Building ABS API first ..."
cd ..
git clone https://github.com/CrispOSS/abs-api || true
cd abs-api
mvn -q clean install -DskipTests
cd ..

echo "Building Prime Sieves"
cd $DIR
mvn -q clean install

echo "Running an example with parameters N=$1 M=$2"

cd target
unzip -q abs-api-prime-sieves-*.zip

n=$1
m=$2
java -Xmx2048m -jar abs-api-prime-sieves-1.0-SNAPSHOT.jar $n $m
