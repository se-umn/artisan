#!/bin/bash
while read -r TEST_NAME; do
    for TEST_FILE  in $(find  . -iname "${TEST_NAME}.java"); do
      echo "Deleting Broken Test ${TEST_FILE}" 
      mv ${TEST_FILE} ${TEST_FILE}.broken
    done
done< <(cat carved-tests.log | grep "error: " | tr "." " " | awk '{print $1}' | tr "/" " " | awk '{print $NF}' | sort | uniq)
# Report the count
BROKEN_TESTS=$(cat carved-tests.log | grep "error: " | tr "." " " | awk '{print $1}' | tr "/" " " | awk '{print $NF}' | sort | uniq | wc -l)
echo "Broken tests: ${BROKEN_TESTS}" >> carving.log 
