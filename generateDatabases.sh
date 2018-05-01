#!/bin/bash

if [ -e generateIndexes.jar ]; then
	echo "Recycling generateIndexes.jar..."
else
	echo "Compiling generateIndexes.jar..."
	build-index-generator.sh
fi

tic=`date +%s`
for key in $(java -jar generateIndexes.jar); 
do 
	./getData.sh $key; 
done
toc=`date +%s`

runtime=$((toc-tic))

echo "Ellapsed time: "$runtime"ms"
