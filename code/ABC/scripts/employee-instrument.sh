#!/bin/bash

LOG="./logs/employee-instrument.log"

rm -r ./sootOutput
rm ${LOG}

export JAVA_OPTS="-Dtrace.output=./tracingOut -Ddump.output=./tracingOut"

PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar"

./abc.sh instrument \
	${PROJECT_JAR} 2>&1 | \
		tee ${LOG}
