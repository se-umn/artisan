#!/bin/bash

rm employee-carved-tests.log

CARVED_TESTS_CP=${1:-./abcOutput}

PROJECT_CP="/Users/gambi/.m2/repository/Callgraph_TestCases/Employee/0.0.1-SNAPSHOT/Employee-0.0.1-SNAPSHOT.jar"

JUNIT_CP="/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

# Trace requires Xstream and XPull to dump to file
SUPPORTING_JARS="../libs/trace.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xmlpull-1.1.3.1.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xpp3_min-1.1.4c.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xstream-1.4.10.jar"


set -e
set -x

# Recompile the tests
SOURCE_CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f | tr "\n" " ")

javac \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        ${SOURCE_CARVED_TESTS} | \
            tee employee-carved-tests.log

CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.java||g' | tr "\n" " ")

java \
	-cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
		${JAVA_OPTS} \
		org.junit.runner.JUnitCore \
			${CARVED_TESTS} | \
				tee -a employee-carved-tests.log