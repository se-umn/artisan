#!/bin/bash

CARVED_TEST_LOG=./logs/hotelme-carved-tests-coverage.log
JACOCO_EXEC=./coverage/hotelme-carved-tests.exec
JACOCO_XML_REPORT=./coverage/hotelme-carved-tests.xml
JACOCO_HTML_REPORT=./coverage/hotelme-carved-tests-report

rm ${CARVED_TEST_LOG}
rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}



CARVED_TESTS_CP=${1:-./abcOutput}

PROJECT_JAR="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT-tests.jar"

PROJECT_CP="${PROJECT_JAR}:/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar:/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar"

JUNIT_CP="/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

# Trace requires Xstream and XPull to load from file
SUPPORTING_JARS="../libs/trace.jar:../src/test/resources/xmlpull-1.1.3.1.jar:../src/test/resources/xpp3_min-1.1.4c.jar:../src/test/resources/xstream-1.4.10.jar"

# Recompile the tests to avoid the Verify and other bytecore related problem
SOURCE_CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f | tr "\n" " ")
javac \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        ${SOURCE_CARVED_TESTS}


CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.java||g' | tr "\n" " ")

# Collect Passing/Failing values
#java \
#	-Xverify:none \
#	-cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
#		${JAVA_OPTS} \
#		org.junit.runner.JUnitCore \
#			${CARVED_TESTS} | \
#				tee -a hotelme-carved-tests.log

JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

### Collect coverage information. This works only if tests pass, isn't it?.
java \
    -javaagent:${JACOCO_AGENT}=destfile=${JACOCO_EXEC},excludes=org.hotelme.Main \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
    ${JAVA_OPTS} \
        org.junit.runner.JUnitCore \
            ${CARVED_TESTS} \
                2>&1 | tee ${CARVED_TEST_LOG}

set -x


### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \
    --classfiles ${PROJECT_JAR} \
    --name "Hotel Reservation Carved Tests Coverage Report" \
    --html ${JACOCO_HTML_REPORT} \
    --xml ${JACOCO_XML_REPORT}
