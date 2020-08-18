# Basic Calculator App
* We use this app to develop the tool for our tecnique.

# Build and Run Tests on Instrumented Version

Build app:

```
./gradlew assembleDebug
```

Instrument and sign the app:

```
abc instrument-apk ./app/build/outputs/apk/debug/app-debug.apk

mv ../../../action-based-test-carving/code/ABC/instrumentation/instrumented-apks/app-debug.apk app-debug-instrumented.apk

```

Install the instrumented app in the emulator

```
abc install-apk app-debug-instrumented.apk
```

app-debug-androidTest.apk 


Build tests:

```
./gradlew assembleAndroidTest
    
mv ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-debug-androidTest.apk
```

Sign and install tests:
    
```
abc sign-apk app-debug-androidTest.apk  

abc install-apk app-debug-androidTest.apk
```

Run all tests:

```
adb shell am instrument -w -r abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner
```

Run only one test (i.e., `abc.basiccalculator.MainActivityTest.testCalculate`):

```
adb shell am instrument -w -e class abc.basiccalculator.MainActivityTest#testCalculate abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner
```

Collect the traces for the executions:

```
abc copy-traces abc.basiccalculator
mv /Users/gambi/action-based-test-carving/code/ABC/carving/traces/abc.basiccalculator/Trace-* .
```



# Build and Run Tests on regular version
* Build
	* Run
		* `./gradlew assembleDebug`
* Test
	* Turn on emulator running Android 28
	* Run:
		* `./gradlew connectedAndroidTest`


