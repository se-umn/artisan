#!/bin/bash

if [ $# -lt 1 ]; then echo "missing trace file"; exit 1; fi

TRACE_FILE=$1

MUT=${2:-'<org.employee.Validation: int numberValidation(java.lang.String)>'}

./abc.sh carve \
	'/Users/gambi/action-based-test-carving/test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar' \
	${TRACE_FILE} \
	"${MUT}"
