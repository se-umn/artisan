#!/bin/bash

rm -r employee-tracingOut

if [ ! -d ./employee-sootOutput ]; then
	(>&2 echo "Instrumented files are not available !")
	exit 1
fi

if [ ! -d ./logs ]; then
	mkdir ./logs
fi


INST_SYSTEM_TESTS_LOG=./logs/employee-system-tests.log
SYSTEM_TESTS_LOG=./logs/employee-system-tests-coverage.log

JACOCO_EXEC=./coverage/employee-system-tests.exec
JACOCO_XML_REPORT=./coverage/employee-system-tests.xml
JACOCO_HTML_REPORT=./coverage/employee-system-tests-report

# CLEAN UP FILES
rm ${SYSTEM_TESTS_LOG}
rm ${INST_SYSTEM_TESTS_LOG}

rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}

# Invoke the System test using JUnit. It requires all the test deps to be provided as classpath

# Note that sootOutpu, which contains the instrumented files, must come BEFORE the original project.

INSTR_CP="./employee-sootOutput"

# We require system test cases packages as jar
PROJECT_CP="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

PROJECT_SRC="../../../test-subjects/Examples/Employee/src"

# We require JUnit and Hamcrest for the tests, plus system-rules for mocking input to System.in
JUNIT_CP=$(find /home/alessio/.m2/repository -iname "junit-4.12.jar")":"\
$(find /home/alessio/.m2/repository -iname "hamcrest-core-1.3.jar")":"\
$(find /home/alessio/.m2/repository -iname "system-rules-1.17.0.jar")

# ABC Tracing requires Xstream, XPull, and Xpp3 to dump object instances to XML files
SUPPORTING_JARS="../libs/trace.jar:"\
$(find /home/alessio/.m2/repository -iname "xmlpull-1.1.3.1.jar")":"\
$(find /home/alessio/.m2/repository -iname "xstream-1.4.10.jar")":"\
$(find /home/alessio/.m2/repository -iname "xpp3_min-1.1.4c.jar")

# Here's the list of system tests to execute
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestAdminLoginWithEmptyDb"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestAdminLoginWithNonEmptyDb"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestEmployeeLogin"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSeniorSoftwareEnginner"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSoftwareEnginner"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSoftwareTrainee"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestStartAndExit"

# Code coverage libraries
JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

#### Collect coverage information BEFORE running the instrumented code
java \
    -javaagent:${JACOCO_AGENT}=destfile=${JACOCO_EXEC},excludes=org.employee.Employee \
        -cp ${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        ${JAVA_OPTS} \
            org.junit.runner.JUnitCore \
                ${SYSTEM_TESTS} \
                    2>&1 | tee ${SYSTEM_TESTS_LOG}

### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \
    --sourcefiles ${PROJECT_SRC} \
    --classfiles ${PROJECT_CP} \
    --name "Employee System Tests Coverage Report" \
    --html ${JACOCO_HTML_REPORT} \
    --xml ${JACOCO_XML_REPORT}

### Run the instrumented files to generate the trace. Those are default folder
JAVA_OPTS="-Dtrace.output=./employee-tracingOut -Ddump.output=./employee-tracingOut"

java \
	-cp ${INSTR_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
		${JAVA_OPTS} \
		org.junit.runner.JUnitCore \
			${SYSTEM_TESTS} \
                2>&1 | tee ${INST_SYSTEM_TESTS_LOG}
