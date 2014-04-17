#/bin/bash

CWD=$(pwd)
LIBS=lib/*
SOURCE="$CWD/src/george/*.java"
javac -cp .:$LIBS $SOURCE -d class -Xlint:deprecation -Xlint:unchecked
