# !/bin/bash
curl -s 'https://finance.yahoo.com/quote/'$1 | grep "root.*main" | perl -pe 's/,/\n/g' | egrep regularMarketPrice | perl -pe 's/.*raw.://g' | head -1
