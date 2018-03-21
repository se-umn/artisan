#!/bin/bash

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR=./abcOutput
LOG="${LOG_FOLDER}/employee-carving.log"


# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar ../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

DEFAULT_CARVE_BY="package=org.employee"

# Note that org.junit.rules.TemporaryFolde is broken at the moment
DEFAULT_EXTERNAL_INTERFACES="java.util.Scanner org.junit.rules.TemporaryFolder java.nio.file.Files"

# Inputs
TRACE_FILE=${1-"./tracingOut/trace.txt"}
CARVE_BY=${2:-"${DEFAULT_CARVE_BY}"}
EXTERNAL_INTERFACES=${3:-"${DEFAULT_EXTERNAL_INTERFACES}"}

# Additional options for the carve command
export JAVA_OPTS="-Dorg.slf4j.simpleLogger.defaultLogLevel=TRACE"

set -x

${BIN_FOLDER}/carve \
            --carve-by ${CARVE_BY} \
                --trace-file ${TRACE_FILE} \
                --project-jar ${PROJECT_JAR} \
                --external ${EXTERNAL_INTERFACES} \
                --output-to ${OUTPUT_DIR} \
                2>&1 | tee ${LOG}
