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

EXCLUDE="package=org.hotelme.systemtests package=org.hotelme.utils class=org.hotelme.Main"

# Inputs
TRACE_FILE="./hotelme-tracingOut/trace.txt"
CARVE_BY="package=org.hotelme"
EXTERNAL_INTERFACES="package=java.nio.file class=java.util.Scanner package=java.sql class=java.io.File"

# Additional options for the carve command - mostly random choice for CG
#  -verbose:gc
export JAVA_OPTS="-Xmx4g -Xms512m -XX:+UseParallelGC -XX:NewRatio=2 -Dorg.slf4j.simpleLogger.defaultLogLevel=INFO"

RESET_ENV="--reset-environment-by org.hotelme.utils.SystemTestUtils.dropAndRecreateTheDb()"

${BIN_FOLDER}/carve \
            --carve-by ${CARVE_BY} \
		--skip-minimize \
                --trace-file ${TRACE_FILE} \
                --project-jar $(echo ${PROJECT_CP}| tr ":" " ") \
                --external ${EXTERNAL_INTERFACES} \
                --exclude-by ${EXCLUDE} \
                --output-to ${OUTPUT_DIR} \
		${RESET_ENV} \
                2>&1 | tee ${LOG}
