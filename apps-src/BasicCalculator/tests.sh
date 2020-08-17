#!/bin/bash

# Run one test.
# adb install app-debug-instrumented.apk
# adb install app-debug-androidTest.apk
# adb shell am instrument -w -r abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner
# /Users/gambi/Library/Android/sdk/platform-tools/adb shell am instrument -w -r abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner
#
TEST_CLASS="abc.basiccalculator.MainActivityTest"
#TEST_NAME="#testCalculate"

set -x

/Users/gambi/Library/Android/sdk/platform-tools/adb shell am instrument -w -e \
	class ${TEST_CLASS}${TEST_NAME} abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner

set +x
