#!/bin/bash
# ABC Framework script

INSTRUMENT_JAVA_OPTS="-Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE"
# -Ddebug=true"

DEFAULT_INSTRUMENT_OUTPUT_FORMAT="class" # "jimple"

BIN_FOLDER="../target/appassembler/bin"

function instrument(){
	
	rm instrument.log
	
	if [ $# -lt 1 ]; then
		(>&2 echo "Not enough arguments. Missing project jar")
	fi
	 
	PROJECT_JAR=$1
	
	export JAVA_OPTS=${INSTRUMENT_JAVA_OPTS}
	
	${BIN_FOLDER}/instrument \
		 ${PROJECT_JAR} ${DEFAULT_INSTRUMENT_OUTPUT_FORMAT} 2>&1 | \
			tee instrument.log
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
