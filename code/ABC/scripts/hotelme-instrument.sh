#!/bin/bash

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR="./hotelme-sootOutput"
LOG="${LOG_FOLDER}/hotelme-instrument.log"
DEFAULT_INSTRUMENT_OUTPUT_FORMAT="class"

# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

# We require system test cases packages as jar
# We require system test cases packages as jar
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

EXTERNAL_INTERFACES="java.io.File java.nio.Path java.util.Scanner java.nio.file.Files"

export JAVA_OPTS="-Dtrace.output=./hotelme-tracingOut -Ddump.output=./hotelme-tracingOut"

set -x

${BIN_FOLDER}/instrument \
    --project-jar $(echo ${PROJECT_CP} | tr ":" " ") \
    --exclude "com.*" "org.*" \
    --include "org.hotelme.*" "org.hotelme.systemtests.*" "org.hotelme.utils.*" \
    --output-to ${OUTPUT_DIR} \
    --output-type ${DEFAULT_INSTRUMENT_OUTPUT_FORMAT} \
        2>&1 | tee ${LOG}
