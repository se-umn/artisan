#!/bin/bash
while read -r TEST_NAME; do
    for TEST_FILE  in $(find  . -iname "${TEST_NAME}.java"); do
      echo "Deleting ${TEST_FILE}" 
      mv ${TEST_FILE} ${TEST_FILE}.failed
    done
done< <(cat carved-tests.log | grep FAILED | grep "\.Test" | awk '{print $1}' | tr "." " " | awk '{print $NF}' | sort | uniq)
# Report the count
FAILED_TESTS=$(cat carved-tests.log | grep FAILED | grep "\.Test" | awk '{print $1}' | tr "." " " | awk '{print $NF}' | sort | uniq | wc -l)
echo "Failed tests: ${FAILED_TESTS}" >> carving.log 