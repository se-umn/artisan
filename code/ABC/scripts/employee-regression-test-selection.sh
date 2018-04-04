#!/bin/bash

unameOut="$(uname -s)"
case "${unameOut}" in
Linux*)     machine=Linux; doShuffle='shuf'; sedOption='-i';;
Darwin*)    machine=Mac; doShuffle='gshuf'; sedOption='-i ""';;
*)          machine="UNKNOWN:${unameOut}"
esac
echo ${machine}

# Run Ekstazi on original
HERE=$(pwd)

EKSTAZI=${HERE}/../libs/org.ekstazi.core-5.2.0.jar

PROJECT_JAR="./target/Employee-0.0.1-SNAPSHOT.jar"
TEST_JAR="./target/Employee-0.0.1-SNAPSHOT-tests.jar"
CARVED_TESTS_CP="${HERE}/employee-abcOutput"

PROJECT_CP="${PROJECT_JAR}:${TEST_JAR}"

#"/Users/gambi/.m2/repository/junit/junit/4.12/junit-4.12.jar:/Users/gambi/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/Users/gambi/.m2/repository/com/github/stefanbirkner/system-rules/1.17.0/system-rules-1.17.0.jar"

SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestAdminLoginWithEmptyDb"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestAdminLoginWithNonEmptyDb"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestEmployeeLogin"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSeniorSoftwareEnginner"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSoftwareEnginner"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestRegisterANewSoftwareTrainee"
SYSTEM_TESTS="${SYSTEM_TESTS} org.employee.systemtest.TestStartAndExit"


DATA_FILE=${HERE}/employee-rts.csv
rm ${DATA_FILE}

CARVED_TESTS=$(find ${CARVED_TESTS_CP} -iname "Test*.class" -type f | sed "s|${CARVED_TESTS_CP}/||"| tr "/" "." | sed 's|\.class||g' | tr "\n" " ")

cd Employee

if [ ! -f cp.txt ]; then
echo "Build CP"
mvn dependency:build-classpath -Dmdep.outputFile=cp.txt
fi

JUNIT_CP=$(cat cp.txt | tr "\n" ":")


echo "Reset project"
ORIGINAL_SHA=$(git rev-list --max-parents=0 HEAD)
git reset --hard ${ORIGINAL_SHA}
git checkout .

# Do not consider the MAIN
# Do not consider interfaces, otherwise we need to changes all its implementations !
echo "Collect files in the project"
i=0
while read -r file; do
    if [[ -f $file ]]; then
        array[$i]=$file
        i=$(($i+1))
    fi
done < <(find src/main/java -iname "*.java" -type f \
    -not -iname "Employee.java" \
    -not -iname "FileManagement.java" )

echo "Found ${i} src files in the project"

##### Original Version

echo "Repackage"
mvn -q clean package -DskipTests

echo "Remove Ekstazi Folder"
rm -rf .ekstazi
rm -rf .ekstazi-system
rm -rf .ekstazi-carved

echo "Running System Tests"
TOTAL_SYSTEM_TEST=$(java \
    -cp ${PROJECT_CP}:${JUNIT_CP} \
    -javaagent:${EKSTAZI}=mode=junit \
        org.junit.runner.JUnitCore ${SYSTEM_TESTS} 2>/dev/null | \
            grep OK | sed 's|OK (\(.*\) tests)|\1|')

echo "Ran ${TOTAL_SYSTEM_TEST}"

mv .ekstazi .ekstazi-system

echo "Running Carved Tests"
TOTAL_CARVED_TEST=$(java \
    -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${JUNIT_CP} \
    -javaagent:${EKSTAZI}=mode=junit \
        org.junit.runner.JUnitCore ${CARVED_TESTS} 2>/dev/null | \
            grep OK | sed 's|OK (\(.*\) tests)|\1|')

echo "Ran ${TOTAL_CARVED_TEST}"

mv .ekstazi .ekstazi-carved

##### Start the data collection

CARVED_LOG="carved.log"

REPETITION=20

for N in $(seq  1 $i);
do
    echo "START N $N"
    for R in $(seq 1 $REPETITION); do

        myarray=( $($doShuffle -e "${array[@]}") )
        for FILE in "${myarray[@]:0:$N}"; do
            echo "Modify class file ${FILE}"
            sed $sedOption -e 's|\(^.*class .* {\)|\1 private boolean flag = true;|' ${FILE}
        done

        echo "Repackage"
        mvn -q clean package -DskipTests

        echo "Reset .ekstazi folder to original "
        rm -r .ekstazi
        cp -r .ekstazi-system .ekstazi

        echo "Running system tests"

        N_SYSTEM_TESTS=$(java \
            -cp ${PROJECT_CP}:${JUNIT_CP} \
            -javaagent:${EKSTAZI}=mode=junit \
                org.junit.runner.JUnitCore ${SYSTEM_TESTS} 2>/dev/null | \
                    grep OK | sed 's|OK (\(.*\) test.*|\1|')

        echo "Reset .ekstazi folder to original "
        rm -r .ekstazi
        cp -r .ekstazi-carved .ekstazi

        echo "Running carved tests"

        N_CARVED_TESTS=$(java \
            -cp ${CARVED_TESTS_CP}:${PROJECT_CP}:${JUNIT_CP} \
            -javaagent:${EKSTAZI}=mode=junit \
                org.junit.runner.JUnitCore ${CARVED_TESTS} 2>/dev/null  | \
                    grep OK | sed 's|OK (\(.*\) test.*|\1|')

        echo "$N,$N_SYSTEM_TESTS,$TOTAL_SYSTEM_TEST,$N_CARVED_TESTS,$TOTAL_CARVED_TEST" | tee -a ${DATA_FILE}

        echo "Reset project"
        ORIGINAL_SHA=$(git rev-list --max-parents=0 HEAD)
        git reset --hard ${ORIGINAL_SHA}
        git checkout .

    done
done

cd ${HERE}