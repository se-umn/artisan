#!/bin/bash

rm -r tracingOut

if [ ! -d ./sootOutput ]; then
	(>&2 echo "Instrumented Files are not available !")
	exit 1
fi

rm employee-system-tests.log
rm employee-system-tests-coverage.log
rm employee-system-tests.exec
rm employee-system-tests.xml
rm -r employee-system-tests-report

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


JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

### Collect coverage information BEFORE running the instrumented code
java \
    -javaagent:${JACOCO_AGENT}=destfile=employee-system-tests.exec \
        -cp ${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        ${JAVA_OPTS} \
            org.junit.runner.JUnitCore \
                ${SYSTEM_TESTS} \
                    2>&1 | tee employee-system-tests-coverage.log

### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report employee-system-tests.exec \
    --classfiles ${PROJECT_CP} \
    --name employee-system-tests \
    --html employee-system-tests-report \
    --xml employee-system-tests.xml

### Run the instrumented files to generate the trace
JAVA_OPTS="-Dtrace.output=./tracingOut -Ddump.output=./tracingOut"

java \
	-cp ${INSTR_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
		${JAVA_OPTS} \
		org.junit.runner.JUnitCore \
			${SYSTEM_TESTS} | \
				tee employee-system-tests.log

