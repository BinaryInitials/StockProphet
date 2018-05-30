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

#5 years from today 
START_DATE=$(echo /dev/null/ | awk '{print '$END_DATE'-5*365*24*60*60}')

cookieJar=$(mktemp)
function getCrumb () {
  echo -en "$(curl -s --cookie-jar $cookieJar $1)" | tr "}" "\n" | grep CrumbStore | cut -d':' -f 3 | sed 's/"//g'
}
curl -s --cookie $cookieJar  "https://query1.finance.yahoo.com/v7/finance/download/"$SYMBOL"?period1="$START_DATE"&period2="$END_DATE"&interval=1d&events=history"$BASE_URL"&crumb="$(getCrumb "https://finance.yahoo.com/quote/$SYMBOL/?p=$SYMBOL") > data/$SYMBOL.csv

((ATTEMPTS++))
if grep "cookie" data/$SYMBOL.csv ; then echo $SYMBOL": Cookie Issues, new attempt..."; get-data.sh $1 $ATTEMPTS; else echo $SYMBOL": Success"; fi
