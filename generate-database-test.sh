#!/bin/bash

mkdir -p data

echo "Generating database..."
tic=`date +%s`

./get-data.sh AAPL
./get-data.sh AMZN
./get-data.sh FB
./get-data.sh GOOG
./get-data.sh MCD
./get-data.sh SBUX
 
mv *.json data/
toc=`date +%s`

runtime=$((toc-tic))

echo "Ellapsed time: "$runtime"s"
