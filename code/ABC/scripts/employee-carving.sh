#!/bin/bash

rm -r ./abcOutput

#if [ $# -lt 1 ]; then echo "missing trace file"; exit 1; fi

TRACE_FILE=${1-"./tracingOut/trace.txt"}

#DEFAULT_CARVE_BY='method=<org.employee.Validation: int numberValidation(java.lang.String)>'

DEFAULT_CARVE_BY="package=org.employee"

CARVE_BY=${2:-${DEFAULT_CARVE_BY}}

#export JAVA_OPTS="-Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE"

./abc.sh carve \
	'/Users/gambi/action-based-test-carving/test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar' \
	${TRACE_FILE} \
	"${CARVE_BY}" 2>&1 | \
		tee employee-carving.log
