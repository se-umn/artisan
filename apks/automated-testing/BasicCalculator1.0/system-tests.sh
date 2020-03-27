#!/usr/bin/env bash

# Run the entire system tests by rebuilding everything from scratch
ORIGINAL_APK="$(realpath $(dirname $0)/BasicCalculator.apk)"

# echo "Original APK ${ORIGINAL_APK}"

# abc rebuild_instrument

INSTRUMENTED_APK=$(abc instrument_apk ${ORIGINAL_APK})

# echo "Instrumented APK: ${INSTRUMENTED_APK}"

for TEST in $(find "$(realpath $(dirname $0))" -type f -iname "*.test"); do 
    echo "Running Test: ${TEST}"
    # This might generate more traces? 
    for TRACE in $(abc run_test "${TEST}" "${INSTRUMENTED_APK}"); do
        if [ $((number%2)) -eq 0 ]
        then
            VERDICT="PASS"
        else
            VERDICT="FAIL"
        fi
    done
    echo "Done Test: ${TEST}"
    echo "${VERDICT}"
done
