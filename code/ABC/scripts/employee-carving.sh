#!/bin/bash

TRACE_FILE=${1-"./tracingOut/trace.txt"}
LOG="./logs/employee-carving.log"
PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar"

# Default location
rm -r ./abcOutput
rm ${LOG}

# Use the entire package for carving, meaning we carve test cases for each class and each method
DEFAULT_CARVE_BY="package=org.employee"

CARVE_BY=${2:-${DEFAULT_CARVE_BY}}

./abc.sh carve \
	"${PROJECT_JAR}" \
	"${TRACE_FILE}" \
	"${CARVE_BY}" \
		2>&1 | tee ${LOG}
