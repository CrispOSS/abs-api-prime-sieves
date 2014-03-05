#!/bin/bash

echo "Building ..."
mvn -q clean install
cd target
unzip -q abs-api-prime-sieves-*.zip

n=100000000
print="false"
echo "Starting to run with  p = $n (10^8)" 
java -Xmx10000m -jar abs-api-prime-sieves-1.0-SNAPSHOT.jar $n $print
