# Basic Calculator App
* We use this app to develop the tool for our tecnique.

# Build and Run Tests on Instrumented Version
* Build
	* Run
		* `./gradlew assembleDebug`
* Instrument
	* Use abc script to instrument and install the instrumented version of the app
* Install tests
	* `adb install app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk 
* Run Tests`
	* `adb shell am instrument -w -r abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner`

# Build and Run Tests
* Build
	* Run
		* `./gradlew assembleDebug`
* Test
	* Turn on emulator running Android 28
	* Run:
		* `./gradlew connectedAndroidTest`


