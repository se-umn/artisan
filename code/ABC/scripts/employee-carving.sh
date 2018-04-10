#!/bin/bash

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR="./employee-abcOutput"
LOG="${LOG_FOLDER}/employee-carving.log"


# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar ../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

# Inputs
TRACE_FILE="./employee-tracingOut/trace.txt"

CARVE_BY="package=org.employee"

EXTERNAL_INTERFACES="java.io.File java.nio.Path java.util.Scanner java.nio.file.Files"
EXCLUDE_BY="package=org.employee.systemtest class=org.employee.Employee"
TEST_SETUP_BY="class=org.employee.systemtest.SystemTestUtils"

# Additional options for the carve command - mostly random choice for CG -verbose:gc
export JAVA_OPTS="-Xmx4g -Xms512m -XX:+UseParallelGC -XX:NewRatio=2 -Dorg.slf4j.simpleLogger.defaultLogLevel=INFO"

set -x
${BIN_FOLDER}/carve \
            --carve-by "${CARVE_BY}" \
                --trace-file ${TRACE_FILE} \
                --project-jar ${PROJECT_JAR} \
                --external ${EXTERNAL_INTERFACES} \
                --exclude-by ${EXCLUDE_BY} \
                --test-setup-by ${TEST_SETUP_BY} \
                --output-to ${OUTPUT_DIR} \
                2>&1 | tee ${LOG}
