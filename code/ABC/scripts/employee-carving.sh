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
DEFAULT_EXTERNAL_INTERFACES="java.io.File java.nio.Path java.util.Scanner java.nio.file.Files org.junit.contrib.java.lang.system.ExpectedSystemExit"

# Inputs
TRACE_FILE=${1-"./tracingOut/trace.txt"}
CARVE_BY=${2:-"${DEFAULT_CARVE_BY}"}
EXTERNAL_INTERFACES=${3:-${DEFAULT_EXTERNAL_INTERFACES}}

# Additional options for the carve command - mostly random choice for CG
export JAVA_OPTS="-Xmx4g -Xms512m -XX:+UseParallelGC -XX:NewRatio=2 -verbose:gc
-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO"

set -x

${BIN_FOLDER}/carve \
            --carve-by ${CARVE_BY} \
                --trace-file ${TRACE_FILE} \
                --project-jar ${PROJECT_JAR} \
                --external ${EXTERNAL_INTERFACES} \
                --output-to ${OUTPUT_DIR} \
                2>&1 | tee ${LOG}
