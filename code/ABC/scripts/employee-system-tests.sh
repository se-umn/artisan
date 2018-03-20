#!/bin/bash

rm -r tracingOut

if [ ! -d ./sootOutput ]; then
	(>&2 echo "Instrumented Files are not available !")
	exit 1
fi

rm employee-system-tests.log

# Invoke the System test using JUnit. It requires all the test deps to be provided as classpath

# Note that sootOutpu, which contains the instrumented files, must come before the original project.

INSTR_CP="./sootOutput"

PROJECT_CP="/Users/gambi/.m2/repository/Callgraph_TestCases/Employee/0.0.1-SNAPSHOT/Employee-0.0.1-SNAPSHOT.jar"

TEST_CP="/Users/gambi/.m2/repository/Callgraph_TestCases/Employee/0.0.1-SNAPSHOT/Employee-0.0.1-SNAPSHOT-tests.jar"

JUNIT_CP="/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

# Trace requires Xstream and XPull to dump to file
SUPPORTING_JARS="../libs/trace.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xmlpull-1.1.3.1.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xstream-1.4.10.jar"

SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestAdminLoginWithEmptyDb"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestAdminLoginWithNonEmptyDb"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestEmployeeLogin"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSeniorSoftwareEnginner"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSoftwareEnginner"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSoftwareTrainee"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestStartAndExit"

# Debugging
#SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestStartAndExitWithManualInput"


JAVA_OPTS="-Dtrace.output=./tracingOut -Ddump.output=./tracingOut"

java \
	-cp ${INSTR_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
		${JAVA_OPTS} \
		org.junit.runner.JUnitCore \
			${SYSTEM_TESTS} | \
				tee employee-system-tests.log

