#!/bin/bash

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR=./sootOutput
LOG="${LOG_FOLDER}/employee-instrument.log"
DEFAULT_INSTRUMENT_OUTPUT_FORMAT="class"

# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

# We require system test cases packages as jar
PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar ../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

# Note that org.junit.rules.TemporaryFolde is broken at the moment
DEFAULT_EXTERNAL_INTERFACES="java.util.Scanner org.junit.rules.TemporaryFolder java.nio.file.Files"

EXTERNAL_INTERFACES=${1:-"${DEFAULT_EXTERNAL_INTERFACES}"}

export JAVA_OPTS="-Dtrace.output=./tracingOut -Ddump.output=./tracingOut"

${BIN_FOLDER}/instrument \
    --project-jar ${PROJECT_JAR} \
    --output-to ${OUTPUT_DIR} \
    --output-type ${DEFAULT_INSTRUMENT_OUTPUT_FORMAT} \
        2>&1 | tee ${LOG}
