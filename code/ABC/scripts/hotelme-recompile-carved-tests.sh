#!/bin/bash

CARVED_TEST_LOG=./logs/hotelme-carved-tests-coverage.log
JACOCO_EXEC=./coverage/hotelme-carved-tests.exec
JACOCO_XML_REPORT=./coverage/hotelme-carved-tests.xml
JACOCO_HTML_REPORT=./coverage/hotelme-carved-tests-report

rm ${CARVED_TEST_LOG}
rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}


CARVED_TESTS_CP="./hotelme-abcOutput"

PROJECT_JAR="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT-tests.jar"

PROJECT_CP="${PROJECT_JAR}:/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar:/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar"

JUNIT_CP="/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

# Trace requires Xstream and XPull to load from file
SUPPORTING_JARS="../libs/trace.jar:../src/test/resources/xmlpull-1.1.3.1.jar:../src/test/resources/xpp3_min-1.1.4c.jar:../src/test/resources/xstream-1.4.10.jar"


JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

### Collect coverage information. This works only if tests pass, isn't it?.
set -x

javac \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
            $(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f)