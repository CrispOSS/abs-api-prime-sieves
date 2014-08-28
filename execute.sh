#!/bin/bash


echo "Running an example with parameters N=$1 M=$2"

cd target
#unzip -q abs-api-prime-sieves-*.zip

n=$1
m=$2
time java -Xmx2048m -jar abs-api-prime-sieves-1.0-SNAPSHOT.jar $n $m

