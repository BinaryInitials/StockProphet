#!/bin/bash
folder="temp/"
if [ -z "$1" ];then
	echo "No arguments specified. Creating temp folder"
else
	folder=$(echo $1)
fi
if [ -e $folder ];then
	echo "Recycling "$folder
else
	echo "Creating "$folder"..."
	mkdir $folder
fi
mkdir $folder/css/
mkdir $folder/js/
mkdir $folder/images/

./build-stockprophet.sh
java -jar stockprophet.jar
cp index.php $folder
cp css/* $folder/css
cp js/* $folder/js
cp images/* $folder/images
echo "Website creation successful."
cat $folder/index.php | sed '1,/?>/d'> $folder/index.html
open $folder/index.html