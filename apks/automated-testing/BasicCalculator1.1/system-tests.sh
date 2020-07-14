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
        # Copy the trace in the test folder using the test name as template
        cp -v ${TRACE} ${TEST}-trace

        abc split-trace "$TRACE"
        for SPLIT in "$TRACE"-split*; do
             # Actually check that the number of lines in the trace are even
            number_of_line=$(wc -l ${SPLIT} | awk '{print $1}')
            if [ ! $((number_of_line%2)) -eq 0 ]; then
                # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
                thread_name=$(echo "$SPLIT" | awk 'match($0, /(split\-)[a-zA-Z 0-9 \- _]+$/) {print substr($0, RSTART + 7, RLENGTH)}' | tr "_" " ")
                VERDICT="${red}FAIL${reset}: expected an even number of lines in thread $thread_name but got ${number_of_line}"
                break
            else
                VERDICT="${green}PASS${reset}"
        fi
        done
    done
    echo "Done Test: ${TEST}"
    echo "${VERDICT}"

done

# NOTE: For the moment we stop all the emulators
abc stop-emulator $(abc list-running-emulators)


