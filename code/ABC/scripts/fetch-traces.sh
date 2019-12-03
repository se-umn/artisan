#!/bin/bash

if [ $# -ne 1 ]; 
    then echo "illegal number of parameters"
fi

echo "Fecthing $1"

adb root

adb pull "/data/data/$1"
