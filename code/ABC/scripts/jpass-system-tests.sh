#!/bin/bash
NAME="jpass"

TRACING_OUTPUT="./${NAME}-tracingOut"

rm -r ${TRACING_OUTPUT}

if [ ! -d ./${NAME}-sootOutput ]; then
	(>&2 echo "Instrumented files are not available !")
	exit 1
fi


INST_SYSTEM_TESTS_LOG=./logs/${NAME}-system-tests.log
SYSTEM_TESTS_LOG=./logs/${NAME}-system-tests-coverage.log
JACOCO_EXEC=./coverage/${NAME}-system-tests.exec
JACOCO_XML_REPORT=./coverage/${NAME}-system-tests.xml
JACOCO_HTML_REPORT=./coverage/${NAME}-system-tests-report

# CLEAN UP FILES
rm ${SYSTEM_TESTS_LOG}
rm ${INST_SYSTEM_TESTS_LOG}
rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}

# Invoke the System test using JUnit. It requires all the test deps to be provided as classpath

# Note that sootOutpu, which contains the instrumented files, must come BEFORE the original project.

INSTR_CP="./${NAME}-sootOutput"

# We require system test cases packages as jar
PROJECT_JAR="../../../test-subjects/GUI/jpass/target/jpass-0.1.17-SNAPSHOT.jar"
#PROJECT_SRC="../../../test-subjects/Examples/Employee/src"

# ABC Tracing requires Xstream, XPull, and Xpp3 to dump object instances to XML files
SUPPORTING_JARS="../libs/trace.jar:../src/test/resources/xmlpull-1.1.3.1.jar:../src/test/resources/xstream-1.4.10.jar:../src/test/resources/xpp3_min-1.1.4c.jar"

#### Collect coverage information BEFORE running the instrumented code
#java \
#    -javaagent:${JACOCO_AGENT}=destfile=${JACOCO_EXEC},excludes=org.employee.Employee \
#        -cp ${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
#        ${JAVA_OPTS} \
#            org.junit.runner.JUnitCore \
#                ${SYSTEM_TESTS} \
#                    2>&1 | tee ${SYSTEM_TESTS_LOG}
#
#### Generate the REPORT. HTML for us, XML for later processing
#java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \
#    --sourcefiles ${PROJECT_SRC} \
#    --classfiles ${PROJECT_CP} \
#    --name "${NAME} System Tests Coverage Report" \
#    --html ${JACOCO_HTML_REPORT} \
#    --xml ${JACOCO_XML_REPORT}

### Run the instrumented files to generate the trace. Those are default folder
JAVA_OPTS="-Dtrace.output=${TRACING_OUTPUT} -Ddump.output=${TRACING_OUTPUT}"

set -x
java \
    ${JAVA_OPTS} \
    -cp ${INSTR_CP}:${PROJECT_JAR}:${SUPPORTING_JARS} \
        jpass.JPass

