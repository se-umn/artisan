#!/bin/bash
NAME="jpass"

BIN_FOLDER="../target/appassembler/bin"
LOG_FOLDER="./logs"
OUTPUT_DIR="./${NAME}-sootOutput"
LOG="${LOG_FOLDER}/${NAME}-instrument.log"
DEFAULT_INSTRUMENT_OUTPUT_FORMAT="class"
TRACING_OUTPUT="./${NAME}-tracingOut"

# Default location
rm -r ${OUTPUT_DIR}
rm ${LOG}

# We require system test cases packages as jar
# We run system tests manually
PROJECT_JAR="/Users/gambi/Documents/Passau/Research/action-based-test-carving/test-subjects/GUI/jpass/target/jpass-0.1.17-SNAPSHOT.jar"

EXTERNAL_INTERFACES="java.io.File java.nio.Path java.util.Scanner java.nio.file.Files"

export JAVA_OPTS="-Dtrace.output=./${TRACING_OUTPUT} -Ddump.output=./${TRACING_OUTPUT}"

# "jpass.data.*" "jpass.util.*"
${BIN_FOLDER}/instrument \
    --exclude "com.fasterxml.*" "org.codehaus.*" "com.ctc.*" \
    --project-jar ${PROJECT_JAR} \
    --output-to ${OUTPUT_DIR} \
    --output-type ${DEFAULT_INSTRUMENT_OUTPUT_FORMAT} \
        2>&1 | tee ${LOG}
