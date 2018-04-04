#!/bin/bash

#rm employee-carved-tests.log
CARVED_TEST_LOG=./logs/employee-carved-tests-coverage.log
JACOCO_EXEC=./coverage/employee-carved-tests.exec
JACOCO_XML_REPORT=./coverage/employee-carved-tests.xml
JACOCO_HTML_REPORT=./coverage/employee-carved-tests-report

rm ${CARVED_TEST_LOG}
rm ${JACOCO_EXEC}
rm ${JACOCO_XML_REPORT}
rm -r ${JACOCO_HTML_REPORT}

CARVED_TESTS_CP="./employee-abcOutput"
# Collect the name of tests
CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.class" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.class||g' | tr "\n" " ")

PROJECT_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT.jar"
TEST_JAR="../../../test-subjects/Examples/Employee/target/Employee-0.0.1-SNAPSHOT-tests.jar"

PROJECT_CP="${PROJECT_JAR}:${TEST_JAR}"

JUNIT_CP="/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

# Trace requires Xstream and XPull to load from file
SUPPORTING_JARS="../libs/trace.jar:../src/test/resources/xmlpull-1.1.3.1.jar:../src/test/resources/xpp3_min-1.1.4c.jar:../src/test/resources/xstream-1.4.10.jar"


JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

# Recompile the tests to avoid problem with verification of bytecode
javac \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
        $(find ${CARVED_TESTS_CP} -iname "Test*.java")

### Collect coverage information. This works only if tests pass, isn't it?.
java \
    -javaagent:${JACOCO_AGENT}=destfile=${JACOCO_EXEC},excludes=org.employee.Employee \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
    ${JAVA_OPTS} \
        org.junit.runner.JUnitCore \
            ${CARVED_TESTS} \
                2>&1 | tee ${CARVED_TEST_LOG}

### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report ${JACOCO_EXEC} \
    --classfiles ${PROJECT_JAR} \
    --name "Employee Carved Tests Coverage Report" \
    --html ${JACOCO_HTML_REPORT} \
    --xml ${JACOCO_XML_REPORT}
