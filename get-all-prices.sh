#!/bin/bash

filename="all-prices.json"
if [ -e $filename ];then
	rm $filename
fi

echo "[" > $filename

tic=`date +%s`
./build-index-generator.sh
for key in $(java -jar generateIndexes.jar); 
do 
	price=$(./get-price.sh $key);
	echo $key":	"$price 
	echo "    {" >> $filename
	echo "       \"name\": \""$key"\"," >> $filename
	echo "       \"price\": \""$price"\"," >> $filename
	echo "    }," >> $filename
done
echo "]" > $filename

toc=`date +%s`
runtime=$((toc-tic))
echo "Ellapsed time: "$runtime"ms"
