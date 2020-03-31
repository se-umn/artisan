#!/usr/bin/env bash

# Run the entire system tests by rebuilding everything from scratch
ORIGINAL_APK="$(realpath $(dirname $0)/BasicCalculator.apk)"

# echo "Original APK ${ORIGINAL_APK}"

# abc rebuild_instrument
INSTRUMENTED_APK=$(abc instrument-apk ${ORIGINAL_APK})

# echo "Instrumented APK: ${INSTRUMENTED_APK}"

red=`tput setaf 1`
green=`tput setaf 2`
reset=`tput sgr0`

for TEST in $(find "$(realpath $(dirname $0))" -type f -iname "*.test"); do 
    echo "Running Test: ${TEST}"
    # This might generate more traces? 
    for TRACE in $(abc run-test "${TEST}" "${INSTRUMENTED_APK}" ); do
        if [ $((number%2)) -eq 0 ]
        then
            # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
            VERDICT="${green}PASS${reset}"
        else
            VERDICT="${red}FAIL${reset}"
        fi
    done
    echo "Done Test: ${TEST}"
    echo "${VERDICT}"
done

# NOTE: For the moment we stop all the emulators
abc stop-emulator $(abc list-running-emulators)


