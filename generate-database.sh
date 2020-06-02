#!/bin/bash

rm -rf failed_attempts.txt

echo "Compiling generateIndexes.jar..."
./build-index-generator.sh

mkdir -p data

echo "Generating database..."
tic=`date +%s`
for key in $(java -jar generateIndexes.jar | awk '{print $1}' | grep -iv "\."); 
do 
	./get-data.sh $key;
done
for key in $(java -jar generateIndexes.jar | awk '{print $1}' | grep "\." | perl -pe 's/\./-/g'); 
do 
	./get-data.sh $key; 
done

for key in $(cat failed_attempts.txt);
do
	./get-data.sh $key;
done

mv *.json data/
toc=`date +%s`

runtime=$((toc-tic))
echo "Ellapsed time: "$runtime"s"
