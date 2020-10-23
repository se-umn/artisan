GW=./gradlew
ABC=/Users/gambi/bin/abc
ABC_CFG="/Users/gambi/action-based-test-carving/scripts/.abc-config"
JAVA_OPTS=" -Dabc.instrument.fields.operations -Dabc.taint.android.intents "

.PHONY: clean-gradle

clean-gradle:
	$(GW) clean


app-original.apk : 
	export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) assemble && \
	mv app/build/outputs/apk/toInstrument/app-toInstrument-unsigned.apk . && \
	$(ABC) sign-apk app-toInstrument-unsigned.apk && \
	mv -v app-toInstrument-unsigned.apk app-original.apk


app-instrumented.apk : app-original.apk
	export ABC_CONFIG=$(ABC_CFG) && \
	export JAVA_OPTS=$(JAVA_OPTS) && \
	$(ABC) instrument-apk app-original.apk && \
	mv -v /Users/gambi/action-based-test-carving/code/ABC/instrumentation/instrumented-apks/app-original.apk app-instrumented.apk


app-androidTest.apk :
	export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) assembleAndroidTest && \
	mv ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-androidTest-unsigned.apk && \
	$(ABC) sign-apk app-androidTest-unsigned.apk && \
	mv -v app-androidTest-unsigned.apk app-androidTest.apk


run-espresso-tests : app-original.apk app-androidTest.apk
	export ABC_CONFIG=$(ABC_CFG) && \
	$(ABC) start-clean-emulator && \
	$(ABC) install-apk app-original.apk && \
	$(ABC) install-apk app-androidTest.apk && \
	 /Users/gambi/Library/Android/sdk/platform-tools/adb shell am instrument -w -r abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner | tee espresso-tests.log && \
	$(ABC) stop-all-emulators

trace-espresso-tests : app-instrumented.apk app-androidTest.apk
	export ABC_CONFIG=$(ABC_CFG) && \
	$(ABC) start-clean-emulator && \
	$(ABC) install-apk app-instrumented.apk && \
	$(ABC) install-apk app-androidTest.apk && \
	 /Users/gambi/Library/Android/sdk/platform-tools/adb shell am instrument -w -r abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner | tee instrumentation-tests.log && \
	$(ABC) copy-traces abc.basiccalculator ./traces force-clean && \
	$(ABC) stop-all-emulators

carve-all : traces app-original.apk
	export ABC_CONFIG=$(ABC_CFG) && \
	$(ABC) carve-all app-original.apk traces app/src/carvedTest force-clean | tee carving.log

run-all-carved-tests : app/src/carvedTest
	for testName in $$(find app/src/carvedTest -type d -depth 1 -exec basename {} \;); do $(GW) test -PcarvedTests -PtestName=$${testName} | tee $${testName}.log; done