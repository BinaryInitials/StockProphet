#!/bin/bash

echo "Compiling generateIndexes.jar..."
./build-index-generator.sh

mkdir -p data

echo "Generating database..."
tic=`date +%s`
for key in $(java -jar generateIndexes.jar); 
do 
	./getData.sh $key; 
done
toc=`date +%s`

runtime=$((toc-tic))

echo "Ellapsed time: "$runtime"ms"
