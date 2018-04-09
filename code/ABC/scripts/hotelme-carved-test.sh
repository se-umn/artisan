#!/bin/bash

CARVED_TEST_LOG=./logs/hotelme-carved-tests-coverage.log

JACOCO_EXEC=./coverage/hotelme-carved-tests.exec
JACOCO_XML_REPORT=./coverage/hotelme-carved-tests.xml
JACOCO_HTML_REPORT=./coverage/hotelme-carved-tests-report
REPORT_NAME="Hotel Reservation Carved Tests Coverage Report"

MIN_JACOCO_EXEC=./coverage/hotelme-carved-tests_minimized.exec
MIN_JACOCO_XML_REPORT=./coverage/hotelme-carved-tests_minimized.xml
MIN_JACOCO_HTML_REPORT=./coverage/hotelme-carved-tests-report_minimized
MIN_REPORT_NAME="Hotel Reservation Minimized Carved Tests Coverage Report"

rm ${CARVED_TEST_LOG}

rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}

rm ${MIN_JACOCO_EXEC}
rm ${MIN_JACOCO_XML_REPORT}
rm -r ${MIN_JACOCO_HTML_REPORT}


CARVED_TESTS_CP="./hotelme-abcOutput"
CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -not -iname "*_minimized*" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.java||g' | tr "\n" " ")
MIN_CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -iname "*_minimized*" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.java||g' | tr "\n" " ")

PROJECT_JAR="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT-tests.jar"

DEPS=$(find ${HOME}/.m2/repository -iname "joda-time-2.9.4.jar")":"\
$(find ${HOME}/.m2/repository -iname "mysql-connector-java-5.1.39.jar")

# We require JUnit and Hamcrest for the tests, plus system-rules for mocking input to System.in
JUNIT_CP=$(find /home/alessio/.m2/repository -iname "junit-4.12.jar")":"\
$(find /home/alessio/.m2/repository -iname "hamcrest-core-1.3.jar")":"\
$(find /home/alessio/.m2/repository -iname "system-rules-1.17.0.jar")

# ABC Tracing requires Xstream, XPull, and Xpp3 to dump object instances to XML files
SUPPORTING_JARS="../libs/trace.jar:"\
$(find /home/alessio/.m2/repository -iname "xmlpull-1.1.3.1.jar")":"\
$(find /home/alessio/.m2/repository -iname "xstream-1.4.10.jar")":"\
$(find /home/alessio/.m2/repository -iname "xpp3_min-1.1.4c.jar")

PROJECT_CP="${PROJECT_JAR}:${TEST_CP}:${DEPS}"

JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

# Recompile the tests to avoid problem with verification of bytecode
javac \
-cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
	$(find ${CARVED_TESTS_CP} -iname "Test*.java") 2>&1 | \
		tee ${CARVED_TEST_LOG}


### Collect coverage information. This works only if tests pass, isn't it?.
java \
    -javaagent:${JACOCO_AGENT}=destfile=${JACOCO_EXEC},excludes=org.hotelme.Main,append=true \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
    ${JAVA_OPTS} \
        org.junit.runner.JUnitCore \
            ${CARVED_TESTS} \
                2>&1 | tee -a ${CARVED_TEST_LOG}

### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \
    --classfiles ${PROJECT_JAR} \
    --name "${REPORT_NAME}" \
    --html ${JACOCO_HTML_REPORT} \
    --xml ${JACOCO_XML_REPORT}

### Collect coverage information. This works only if tests pass, isn't it?.
java \
    -javaagent:${JACOCO_AGENT}=destfile=${MIN_JACOCO_EXEC},excludes=org.hotelme.Main,append=true \
        -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        ${JAVA_OPTS} \
        org.junit.runner.JUnitCore \
            ${MIN_CARVED_TESTS} 2>&1 | \
                tee -a ${CARVED_TEST_LOG}

### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report ${MIN_JACOCO_EXEC} \
    --classfiles ${PROJECT_JAR} \
    --name "${MIN_REPORT_NAME}" \
    --html ${MIN_JACOCO_HTML_REPORT} \
    --xml ${MIN_JACOCO_XML_REPORT}
