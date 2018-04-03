#!/bin/bash

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR="./hotelme-abcOutput"
LOG="${LOG_FOLDER}/hotelme-carving.log"


# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

PROJECT_JAR="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT.jar"
TEST_CP="../../../test-subjects/CommandLineUtilities/HotelMe/target/HotelReservationSystem-0.0.1-SNAPSHOT-tests.jar"

PROJECT_CP="${PROJECT_JAR}:${TEST_CP}:/Users/gambi/.m2/repository/joda-time/joda-time/2.9.4/joda-time-2.9.4.jar:/Users/gambi/.m2/repository/mysql/mysql-connector-java/5.1.39/mysql-connector-java-5.1.39.jar"

EXCLUDE="package=org.hotelme.systemtests package=org.hotelme.utils class=org.hotelme.Main"

# Inputs
TRACE_FILE="./hotelme-tracingOut/trace.txt"
CARVE_BY="package=org.hotelme"
EXTERNAL_INTERFACES="package=java.nio.file class=java.util.Scanner package=java.sql class=java.io.File"

# Additional options for the carve command - mostly random choice for CG
export JAVA_OPTS="-Xmx4g -Xms512m -XX:+UseParallelGC -XX:NewRatio=2 -verbose:gc
-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO"

set -x

${BIN_FOLDER}/carve \
            --carve-by ${CARVE_BY} \
                --trace-file ${TRACE_FILE} \
                --project-jar $(echo ${PROJECT_CP}| tr ":" " ") \
                --external ${EXTERNAL_INTERFACES} \
                --test-setup-by "class=org.hotelme.utils.SystemTestUtils" \
                --exclude-by ${EXCLUDE} \
                --output-to ${OUTPUT_DIR} \
                2>&1 | tee ${LOG}
