#!/bin/bash
./clean.sh
git add .
git commit -m $0
git push -u origin master
