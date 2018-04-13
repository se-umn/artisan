#!/bin/bash

rm -r hotelme-tracingOut

if [ ! -d ./hotelme-sootOutput ]; then
	(>&2 echo "Instrumented Files are not available !")
	exit 1
fi


INST_SYSTEM_TESTS_LOG=./logs/hotelme-system-tests.log
SYSTEM_TESTS_LOG=./logs/hotelme-system-tests-coverage.log
JACOCO_EXEC=./coverage/hotelme-system-tests.exec
JACOCO_XML_REPORT=./coverage/hotelme-system-tests.xml
JACOCO_HTML_REPORT=./coverage/hotelme-system-tests-report

# CLEAN UP FILES
rm ${SYSTEM_TESTS_LOG}
rm ${INST_SYSTEM_TESTS_LOG}
rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}

# Invoke the System test using JUnit. It requires all the test deps to be provided as classpath

# Note that sootOutpu, which contains the instrumented files, must come BEFORE the original project.

INSTR_CP="./hotelme-sootOutput"

# We require system test cases packages as jar
PROJECT_JAR="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT-tests.jar"

DEPS=$(find ${HOME}/.m2/repository -iname "joda-time-2.9.4.jar")":"\
$(find ${HOME}/.m2/repository -iname "mysql-connector-java-5.1.39.jar")

# We require JUnit and Hamcrest for the tests, plus system-rules for mocking input to System.in
JUNIT_CP=$(find ${HOME}/.m2/repository -iname "junit-4.12.jar")":"\
$(find ${HOME}/.m2/repository -iname "hamcrest-core-1.3.jar")":"\
$(find ${HOME}/.m2/repository -iname "system-rules-1.17.0.jar")

# ABC Tracing requires Xstream, XPull, and Xpp3 to dump object instances to XML files
SUPPORTING_JARS="../libs/trace.jar:"\
$(find ${HOME}/.m2/repository -iname "xmlpull-1.1.3.1.jar")":"\
$(find ${HOME}/.m2/repository -iname "xstream-1.4.10.jar")":"\
$(find ${HOME}/.m2/repository -iname "xpp3_min-1.1.4c.jar")


PROJECT_CP="${PROJECT_JAR}:${TEST_CP}:${DEPS}"
echo "${PROJECT_CP}"

# Here's the list of system tests to execute
SYSTEM_TESTS="${SYSTEM_TESTS} org.hotelme.systemtests.TestHotelExit"
SYSTEM_TESTS="${SYSTEM_TESTS} org.hotelme.systemtests.TestHotelSignUp"
SYSTEM_TESTS="${SYSTEM_TESTS} org.hotelme.systemtests.TestHotelSignUpWithTruncateTable"
SYSTEM_TESTS="${SYSTEM_TESTS} org.hotelme.systemtests.TestHotelReserveRoom"
SYSTEM_TESTS="${SYSTEM_TESTS} org.hotelme.systemtests.TestHotelAlreadyRegistered"


# Code coverage libraries
JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

#### Collect coverage information BEFORE running the instrumented code
java \
-javaagent:${JACOCO_AGENT}=destfile=${JACOCO_EXEC},excludes=org.hotelme.Main \
        -cp ${PROJECT_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        ${JAVA_OPTS} \
            org.junit.runner.JUnitCore \
                ${SYSTEM_TESTS} \
                    2>&1 | tee ${SYSTEM_TESTS_LOG}

# For some reason this does not create an HTML report
### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \
    --classfiles ${PROJECT_JAR} \
    --name "Hotel Reservation System Tests Coverage Report" \
    --html ${JACOCO_HTML_REPORT} \
    --xml ${JACOCO_XML_REPORT}

### Run the instrumented files to generate the trace. Those are default folder
export JAVA_OPTS="-Dtrace.output=./hotelme-tracingOut -Ddump.output=./hotelme-tracingOut"

java \
	-cp ${INSTR_CP}:${PROJECT_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
		${JAVA_OPTS} \
		org.junit.runner.JUnitCore \
			${SYSTEM_TESTS} \
               2>&1 | tee ${INST_SYSTEM_TESTS_LOG}
