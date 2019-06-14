#!/bin/bash
SYMBOL=$1
ATTEMPTS=0
if [ -z "$2" ]
then
	ATTEMPTS=0
else
	ATTEMPTS=$2
fi
if [ $ATTEMPTS -gt 5 ]
then 
	echo $SYMBOL": FAILURE: too many attempts"
	exit
fi

END_DATE=$(date +%s)
START_DATE=$(echo /dev/null/ | awk '{print '$END_DATE'-5*365*24*60*60}')

cookieJar="cookies.txt"

crumb=$(curl -s --cookie-jar $cookieJar https://finance.yahoo.com/quote/$SYMBOL/?p=$SYMBOL | tr "}" "\n" | grep CrumbStore | cut -d':' -f 3 | sed 's/"//g')

url="https://query1.finance.yahoo.com/v7/finance/download/"$SYMBOL"?period1="$START_DATE"&period2="$END_DATE"&interval=1d&events=history&crumb="$crumb
curl -s --cookie $cookieJar $url > $SYMBOL.csv

((ATTEMPTS++))
if grep "cookie" $SYMBOL.csv ; then echo $SYMBOL": Cookie Issues, new attempt..."; ./get-data.sh $1 $ATTEMPTS; else echo $SYMBOL": Success"; fi
