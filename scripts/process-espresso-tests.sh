#!/bin/bash

: ${ABC_HOME:?Please provide a value for ABC_HOME}

# Assume we have all the traces available
start=1
end=3

while read -r espresso_test; do
    rm -f .carved-all
    SIMPLE_NAME=$(echo ${espresso_test} | tr " " "_")
    echo "** Processing ${espresso_test}"
    echo "Simple name ${SIMPLE_NAME}"
    echo "${espresso_test}" > tests.txt
    make carve-all </dev/null
    # Remove broken tests if any and recompute 
    make all-carved-tests-coverage/html/index.html </dev/null
    if [ $(${ABC_HOME}/scripts/delete-broken-tests.sh | wc -l) -gt 1 ]; then
        rm carved-tests.log
        make all-carved-tests-coverage/html/index.html </dev/null
    fi
    # Remove failed tests if any and recompute 
    if [ $(${ABC_HOME}/scripts/delete-failed-tests.sh | wc -l) -gt 1 ]; then
        rm carved-tests.log
        make all-carved-tests-coverage/html/index.html </dev/null
    fi
    # Copy all the logs to the per-test folder
    mv -v carved-tests.log carved-tests.log-for-${SIMPLE_NAME} || echo "(Error ok if no carved-tests.log found)"
    mv -v all-carved-tests-coverage all-carved-tests-coverage-for-${SIMPLE_NAME} || echo "(Error ok if no all-carved-tests-coverage found)"
    mv -v selected-carved-tests-coverage selected-carved-tests-coverage-for-${SIMPLE_NAME} || echo "(Error ok if no selected-carved-tests-coverage found)"
    # Copy all the logs to the per-test folder
    mv -v carving.log carving.log-for-${SIMPLE_NAME} || echo "(Error ok if no carving.log found)"
    # mv the carvedTestFolder
    mv -v app/src/carvedTest carvedTests-for-${SIMPLE_NAME} || echo "(Error copying carved tests)"
    echo "** Done with ${espresso_test}"
done < <(head -$end ./espresso_tests.txt | tail -$(($end - $start + 1)) )
