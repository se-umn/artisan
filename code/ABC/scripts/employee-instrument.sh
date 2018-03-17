#!/bin/bash

rm -r ./sootOutput

export JAVA_OPTS="-Dtrace.output=./tracingOut -Ddump.output=./tracingOut"

./abc.sh instrument \
	'/Users/gambi/action-based-test-carving/test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar' 2>&1 | \
		tee employee-instrument.log
