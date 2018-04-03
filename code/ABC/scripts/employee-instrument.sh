#!/bin/bash

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR="./employee-sootOutput"
LOG="${LOG_FOLDER}/employee-instrument.log"
DEFAULT_INSTRUMENT_OUTPUT_FORMAT="class"

# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

# We require system test cases packages as jar
PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar ../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

EXTERNAL_INTERFACES="java.io.File java.nio.Path java.util.Scanner java.nio.file.Files"

export JAVA_OPTS="-Dtrace.output=./employee-tracingOut -Ddump.output=./employee-tracingOut"

${BIN_FOLDER}/instrument \
    --project-jar ${PROJECT_JAR} \
    --output-to ${OUTPUT_DIR} \
    --output-type ${DEFAULT_INSTRUMENT_OUTPUT_FORMAT} \
        2>&1 | tee ${LOG}