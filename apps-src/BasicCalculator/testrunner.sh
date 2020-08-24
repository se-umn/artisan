#!/usr/bin/env bash

set -euo pipefail

# exit if adb cannot be found
if ! [ -x "$(command -v adb)" ]; then
    printf '%s\n' 'adb not found; ensure it'\''s in your $PATH'
    exit 1
fi

# exit if application name is unspecified (eg, abc.basiccalculator)
if [ -z ${APP_NAME+set} ]; then
    printf '%s\n' '$APP_NAME is empty; set this to the fully qualified name of the application under test'
    exit 1
fi

# exit if apk file is unspecified 
if [ -z ${APK_FILE+set} ]; then
    printf '%s\n' '$APK_FILE is empty; set this to the location of the apk file'
    exit 1
fi

# exit if android jar location is unspecified (eg, abc.basiccalculator)
if [ -z ${ANDROID_JAR+set} ]; then
    printf '%s\n' '$APK_FILE is empty; set this to the location of the Android jar'
    exit 1
fi

# find all files within the current directory, and any subdirs, that contain '@Test'
for file in $(grep -lr '@Test' --include=\*.java .); do

    # find name of class containing test functions
	class=$(printf '%s' "$file" | rev | cut -d '/' -f1 | rev | sed 's/.java//')
    printf '%s\n' "--------------------------------------------------"
    printf '%s\n' "test class: $class"

    # extract function names from test files
    for function in $(grep -A1 -n '@Test' < "$file" | grep 'public void' | sed -e 's/public void //' -e 's/() {//' | tr -s " " - | cut -d '-' -f2); do
        printf '%s\n' "--------------------------------------------------"
        printf '%s' "test function: $function"

        # run test
        adb shell am instrument \
            -w -e class "$APP_NAME"."$class"\#"$function" \
            "$APP_NAME".test/androidx.test.runner.AndroidJUnitRunner

        # extract traces
        pushd ~/action-based-test-carving/scripts/ >/dev/null 2>&1
        ./abc.sh copy-traces "$APP_NAME"
        popd >/dev/null 2>&1

        # clear trace from device, to ensure each trace only captures the execution of each test
        adb shell pm clear "$APP_NAME"

        # find the most recent trace
        TRACE_FILE="$HOME"/action-based-test-carving/code/ABC/carving/traces/"$APP_NAME"/$(ls -t "$HOME"/action-based-test-carving/code/ABC/carving/traces/"$APP_NAME" | sed -n 1p)

        # run the trace parser 
        SEQUENCE_FILE="$PWD"/sequences/method_sequences_"$function"

        # run the verification
        "$HOME"/action-based-test-carving/code/ABC/carving/target/appassembler/bin/verify-trace --android-jar "$ANDROID_JAR" --apk-file "$APK_FILE" --trace-file "$TRACE_FILE" --sequence-file "$SEQUENCE_FILE"
    done
done
