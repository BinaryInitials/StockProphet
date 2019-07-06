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

rm -rf $folder/css/
rm -rf $folder/js/
rm -rf $folder/images/
rm -rf $folder/json

mkdir $folder/css/
mkdir $folder/js/
mkdir $folder/images/
mkdir $folder/json/

./build-stockprophet.sh
java -jar stockprophet.jar
cp *.php $folder
cp css/* $folder/css
cp js/* $folder/js
cp images/* $folder/images

cd data

for file in $(ls data/*.csv); do
  symbol = $(echo $file | perl -pe 's/\.csv//g')
  mv $file $symbol".json"
done
mv *.json ../$folder/
cd ..
echo "Website creation successful."
#cat $folder/index.php | sed '1,/?>/d'> $folder/index.html
#open $folder/index.html
