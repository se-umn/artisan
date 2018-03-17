#!/bin/bash
# ABC Framework script

set -x

INSTRUMENT_JAVA_OPTS="-Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE"
CARVING_JAVA_OPTS="-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG"
# -Ddebug=true"

DEFAULT_INSTRUMENT_OUTPUT_DIR="./sootOutput"
DEFAULT_INSTRUMENT_OUTPUT_FORMAT="class" # "jimple"

DEFAULT_CARVING_OUTPUT_DIR="./abcOutput"

BIN_FOLDER="../target/appassembler/bin"

function instrument(){
	
	rm instrument.log
	
	if [ $# -lt 1 ]; then
		(>&2 echo "Not enough arguments. Missing project jar")
		return
	fi
	 
	PROJECT_JAR=$1
	
	export JAVA_OPTS=${INSTRUMENT_JAVA_OPTS}
	
	${BIN_FOLDER}/instrument \
		--project-jar ${PROJECT_JAR} \
		--output-to ${DEFAULT_INSTRUMENT_OUTPUT_DIR} \
		--output-type ${DEFAULT_INSTRUMENT_OUTPUT_FORMAT} 
}

function carve(){
	if [ $# -lt 1 ]; then
		(>&2 echo "Not enough arguments. Missing project jar, trace file, method name")
		return
	elif [ $# -lt 2 ]; then
		(>&2 echo "Not enough arguments. Missing trace file and method name")
		return
	elif [ $# -lt 3 ]; then
		(>&2 echo "Not enough arguments. Missing method name")
		return
	fi
	
	PROJECT_JAR=$1
	TRACE_FILE=$2
	CARVE_BY=$3
	OUTPUT_DIR=${4:-${DEFAULT_CARVING_OUTPUT_DIR}}
	
	export JAVA_OPTS=${CARVING_JAVA_OPTS}

	${BIN_FOLDER}/carve \
		--carve-by "${CARVE_BY}" \
		--trace-file ${TRACE_FILE} \
		--project-jar ${PROJECT_JAR} \
		--output-to ${OUTPUT_DIR}
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
