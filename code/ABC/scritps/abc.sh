#!/bin/bash
# ABC Framework script

function instrument(){
	echo "TODO"
}

function carve(){
	echo "TODO"
}


function help(){
	echo "COMMANDS"
	cat abc.sh | grep function | grep -v "__private" | grep -v "\#" | sed -e '/^ /d' -e 's|function \(.*\)(){|\1|g'
}


# Invoke functions by name
if declare -f "$1" > /dev/null
then
  # call arguments verbatim
  "$@"
else
  # Show a helpful error
  if [ $# -gt 0 ]; then
    (>&2 echo "'$1' is not a known function name") 
  fi
  help
#  exit 1
fi
