#!/bin/bash

LOG="./logs/employee-instrument.log"

rm -r ./sootOutput
rm ${LOG}

export JAVA_OPTS="-Dtrace.output=./tracingOut -Ddump.output=./tracingOut"

# We require system test cases packages as jar
PROJECT_CP="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

./abc.sh instrument \
        ${PROJECT_CP} ${TEST_CP} 2>&1 | \
		tee ${LOG}
