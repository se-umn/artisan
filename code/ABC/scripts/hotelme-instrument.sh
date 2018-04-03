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

PROJECT_CP="${PROJECT_JAR}:${TEST_CP}:/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar:/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar"

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
