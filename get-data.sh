#!/bin/bash
SYMBOL=$1
ATTEMPTS=0
if [ -z "$2" ]
then
	ATTEMPTS=0
else
	ATTEMPTS=$2
fi
if [ $ATTEMPTS -gt 10 ]
then 
	echo $SYMBOL": FAILURE: too many attempts"
	echo $SYMBOL >> failed_attempts.txt
	exit
fi

END_DATE=$(date +%s)
START_DATE=$(echo /dev/null/ | awk '{print '$END_DATE'-5*365*24*60*60}')

cookieJar="cookies.txt"

crumb=$(curl -s --cookie-jar $cookieJar https://finance.yahoo.com/quote/$SYMBOL/?p=$SYMBOL | tr "}" "\n" | grep CrumbStore | cut -d':' -f 3 | sed 's/"//g')

url="https://query1.finance.yahoo.com/v7/finance/download/"$SYMBOL"?period1="$START_DATE"&period2="$END_DATE"&interval=1d&events=history&crumb="$crumb
curl -s --cookie $cookieJar $url > $SYMBOL.json

((ATTEMPTS++))
if [[ $(cat $SYMBOL.json | wc -l) -lt 20 ]] || grep "cookie" $SYMBOL.json ; then echo $SYMBOL": Cookie Issues, attempt #"$ATTEMPTS; sleep 1; ./get-data.sh $1 $ATTEMPTS; else echo $SYMBOL": Success"; fi
