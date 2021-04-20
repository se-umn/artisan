#!/bin/bash
git remote prune origin

while read -r BRANCH <&3; do
# Confirm delete
read -p "* Delete local branch ${BRANCH}. Continue (y/n)? " choice
case "$choice" in
y|Y ) git branch -d ${BRANCH};;
n|N ) echo "no";;
* ) echo "Invalid: ${choice} Abort"; exit 1;;
esac

done 3< <(git branch -r | awk '{print $1}' | egrep -v -f /dev/fd/0 <(git branch -vv | grep origin) | awk '{print $1}')


