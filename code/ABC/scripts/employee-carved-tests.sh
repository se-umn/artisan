#!/bin/bash

# TODO With Maven this might be way easier

rm employee-carved-tests.log
rm employee-carved-tests-coverage.log


CARVED_TESTS_CP=${1:-./abcOutput}

PROJECT_CP="/Users/gambi/.m2/repository/Callgraph_TestCases/Employee/0.0.1-SNAPSHOT/Employee-0.0.1-SNAPSHOT.jar"

JUNIT_CP="/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

# Trace requires Xstream and XPull to dump to file
SUPPORTING_JARS="../libs/trace.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xmlpull-1.1.3.1.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xpp3_min-1.1.4c.jar:/Users/gambi/Documents/Passau/Research/action-based-test-carving/code/ABC/src/test/resources/xstream-1.4.10.jar"

# Recompile the tests to avoid the Verify problem ?
# SOURCE_CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f | tr "\n" " ")
#javac \
#    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
#        ${SOURCE_CARVED_TESTS} | \
#            tee employee-carved-tests.log

CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.java" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.java||g' | tr "\n" " ")

# Collect Passing/Failing values
java \
	-Xverify:none \
	-cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
		${JAVA_OPTS} \
		org.junit.runner.JUnitCore \
			${CARVED_TESTS} | \
				tee -a employee-carved-tests.log

JACOCO_AGENT="../libs/jacocoagent.jar"
JACOCO_CLI="../libs/jacococli.jar"

### Collect coverage information. This works only if tests pass ?
java \
    -javaagent:${JACOCO_AGENT}=destfile=employee-carved-tests.exec \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${TEST_CP}:${JUNIT_CP}:${SUPPORTING_JARS} \
    ${JAVA_OPTS} \
        org.junit.runner.JUnitCore \
            ${CARVED_TESTS} | \
                tee -a employee-carved-tests-coverage.log

### Generate the REPORT. HTML for us, XML for later processing
java -jar ${JACOCO_CLI} report employee-carved-tests.exec \
    --classfiles ${PROJECT_CP} \
    --name employee-carved-tests \
    --html employee-carved-tests-report \
    --xml employee-carved-tests.xml

