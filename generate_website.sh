#!/bin/bash

build-stockprophet.sh
java -jar stockprophet.jar
cat index.php | sed '1,/?>/d'> index.html
# rm index.php
open index.html
