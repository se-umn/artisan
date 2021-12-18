### MAIN VARIABLES
GW=./gradlew
ABC=../../scripts/abc.sh
ABC_CFG=../../scripts/.abc-config
# -Dabc.make.android.lifecycle.events.explicit
# -Dabc.instrument.array.operations
# -Dabc.instrument.debug -Dabc.instrument.multithreaded"
JAVA_OPTS=" -Dabc.instrument.array.operations -Dabc.instrument.fields.operations -Dabc.taint.android.intents -Dabc.instrument.include=abc.basiccalculator"

ADB := $(shell $(ABC) show-config  ANDROID_ADB_EXE | sed -e "s|ANDROID_ADB_EXE=||")
# Create a list of expected test executions from tests.txt Those corresponds to the traces
ESPRESSO_TESTS := $(shell cat tests.txt | sed '/^[[:space:]]*$$/d' | sed -e 's| |__|g' -e 's|^\(.*\)$$|\1.testlog|')
# Create the list of expected carved targets from tests.txt
ESPRESSO_TESTS_CARVED := $(shell cat tests.txt | sed '/^[[:space:]]*$$/d' | sed -e 's| |__|g' -e 's|^\(.*\)$$|\1.carved|')
# Create the list of expected coverage targets from tests.txt
ESPRESSO_TESTS_COVERAGE := $(shell cat tests.txt | sed '/^[[:space:]]*$$/d' | sed -e 's| |__|g' -e 's|^\(.*\)$$|espresso-test-coverage-for-\1/html/index.html|')


.PHONY: clean-gradle clean-all carve-all run-espresso-tests trace-espresso-tests

show :
	$(info $(ADB))

clean-gradle :
	$(GW) clean

# Debug Target
list-all-tests :
	@echo $(ESPRESSO_TESTS) | tr " " "\n"
	

list-tests : $(ESPRESSO_TESTS)
	@echo $? | tr " " "\n"

list-coverage-targets:
	@ echo $(ESPRESSO_TESTS_COVERAGE) | tr " " "\n"

# Clean up carved tests
clean-carved-tests :
	$(RM) -rv app/src/carvedTest
	$(RM) -rv .carved-all

clean-carved-coverage :
	$(RM) -rv carved-test-coverage


clean-all : clean-carved-tests clean-carved-coverage
# Clean build files
	$(GW) clean
# Clean up apk-related targets
	$(RM) -v *.apk
# Clean up all the logs
	$(RM) -v *.log
# Clean up tracing
	$(RM) -v *.testlog
	$(RM) -rv traces
# Clean up Coverage
	$(RM) -rv espresso-tests-coverage unit-tests-coverage carved-test-coverage
	$(RM) -rv espresso-test-coverage-for-*

# Build the various apks
app-original.apk : 
	export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) -PjacocoEnabled=false assembleDebug && \
	mv ./app/build/outputs/apk/debug/app-debug.apk . && \
	$(ABC) sign-apk app-debug.apk && \
	mv -v app-debug.apk app-original.apk

app-instrumented.apk : app-original.apk
	export ABC_CONFIG=$(ABC_CFG) && \
	export JAVA_OPTS=$(JAVA_OPTS) && \
	$(ABC) instrument-apk app-original.apk && \
	mv -v ../../code/ABC/instrumentation/instrumented-apks/app-original.apk app-instrumented.apk

app-androidTest.apk :
	export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) assembleAndroidTest && \
	mv ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-androidTest-unsigned.apk && \
	$(ABC) sign-apk app-androidTest-unsigned.apk && \
	mv -v app-androidTest-unsigned.apk app-androidTest.apk

## the assembleAndroidTest task also builds the app if it starts from a clean build
app-and-instrumented-tests-for-coverage.apk :
	export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) -PjacocoEnabled=true clean assembleDebug assembleAndroidTest && \
	mv ./app/build/outputs/apk/debug/app-debug.apk app-original-for-coverage.apk  && \
	mv ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-androidTest-for-coverage.apk

# Utility
stop-emulator:
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators 

# Trace all depends on tracing all the tests
trace-all : app-instrumented.apk app-androidTest.apk remove-apk-and-instrumented-tests $(ESPRESSO_TESTS)
# Run the emulator
	@echo "Tracing: $(shell echo $? | tr " " "\n")"
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators 
	@echo "Done"

# Try to trace all tests
$(ESPRESSO_TESTS) :

	$(eval IR_RUNNING := $(shell export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator | wc -l))
	@if [ "$(IR_RUNNING)" == "0" ]; then \
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator; \
	fi

	$(eval FIRST_RUN := $(shell $(ADB) shell pm list packages | grep -c abc.basiccalculator))
	@if [ "$(FIRST_RUN)" == "2" ]; then \
		echo "Resetting the data of the apk"; \
		$(ADB) shell pm clear abc.basiccalculator; \
	else \
	 	echo "Installing instrumented apk" ;\
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-instrumented.apk; \
		echo "Installing test apk" ;\
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-androidTest.apk; \
	fi
#	Evalualte the current test name. Note that test names use #
	$(eval TEST_NAME := $(shell echo "$(@)" | sed -e 's|__|\\\#|g' -e 's|.testlog||'))
	@echo "Tracing test $(TEST_NAME)"
#	Log directly to the expected file
	$(ADB) shell am instrument -w -e class $(TEST_NAME) abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner 2>&1 | tee $(@)
#	Copy the traces if the previous command succeded
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) copy-traces abc.basiccalculator ./traces/$(TEST_NAME) force-clean

# Carving all requires to have all of them traced
# This will always run because it's a phony target
carve-all : .carved-all
	@echo "Carving All"
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators 
	@echo "Done"

.carved-all : $(ESPRESSO_TESTS)
	@echo "Requiement $?"
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) carve-all app-original.apk traces app/src/carvedTest force-clean 2>&1 | tee carving.log
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators 
# 	Make sure this file has a timestamp after the prerequiesing
	@sleep 1; echo "" > .carved-all


run-carved-tests : carvedTests.log
	@echo "Done"

carvedTests.log : .carved-all
	@echo "Done"
	@touch carvedTests.log
# $(GW) -PjacocoEnabled=false clean testDebugUnitTest -PcarvedTests 2>&1 | tee carvedTests.log

# # This run whatever tests is in the carvedTest folder -  UNSAFE
# # Does not ensures carved tests are there but always run
# run-all-existing-carved-tests :
# 	$(GW) -PjacocoEnabled=false clean testDebugUnitTest -PcarvedTests 2>&1 | tee carvedTests.log

### ### ### ### ### ### ### 
### Coverage targets
### ### ### ### ### ### ### 

espresso-test-coverage/html/index.html : remove-apk-and-instrumented-tests
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator
	$(GW) -PjacocoEnabled=true -PcarvedTests=false clean jacocoGUITestCoverage
	$(RM) -r espresso-test-coverage
	mv -v app/build/reports/jacoco/jacocoGUITestCoverage espresso-test-coverage
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators 


coverage-espresso-tests : espresso-test-coverage/html/index.html
	@echo "Done"

clean-coverage-espresso-tests : 
	$(RM) -r espresso-test-coverage


# Required to run each test on its own to compute the coverage report
$(ESPRESSO_TESTS_COVERAGE):
	
	$(eval IR_RUNNING := $(shell export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator | wc -l))
	@if [ "$(IR_RUNNING)" == "0" ]; then \
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator; \
	fi
	
# Starting and restarting the emulator is not robust
	$(eval TEST_NAME := $(shell echo "$(@)" | sed -e 's|__|\\\#|g' -e 's|/html/index.html||' -e 's|espresso-test-coverage-for-||'))
	$(eval COVERAGE_FOLDER := $(shell echo "$(@)" | sed -e 's|/html/index.html||'))
# Ensure we clean up stuff before running each test
	$(eval FIRST_RUN := $(shell $(ADB) shell pm list packages | grep -c abc.basiccalculator))
	@if [ "$(FIRST_RUN)" == "2" ]; then \
		echo "Resetting the data of the apk"; \
		$(ADB) shell pm clear abc.basiccalculator; \
	else \
	 	echo "Installing apk" ;\
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-original-for-coverage.apk; \
		echo "Installing test apk" ;\
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-androidTest-for-coverage.apk; \
	fi
# Execute the gradle target
	@echo "Running Test $(TEST_NAME)"
	$(GW) -PjacocoEnabled=true -PcarvedTests=false -Pandroid.testInstrumentationRunnerArguments.class=$(TEST_NAME) jacocoGUITestCoverage
	mv -v app/build/reports/jacoco/jacocoGUITestCoverage $(COVERAGE_FOLDER)
	
remove-apk-and-instrumented-tests :
	$(eval IR_RUNNING := $(shell export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator | wc -l))
	@if [ "$(IR_RUNNING)" == "0" ]; then \
		export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator; \
	fi
	$(eval APP_INSTALLED := $(shell $(ADB) shell pm list packages | grep -c "abc.basiccalculator$$"))
	@if [ "$(APP_INSTALLED)" == "1" ]; then \
		$(ADB) uninstall abc.basiccalculator; \
	fi
	$(eval INSTRUMENTATION_TESTS_INSTALLED := $(shell $(ADB) shell pm list packages | grep -c "abc.basiccalculator.test$$"))
	@if [ "$(INSTRUMENTATION_TESTS_INSTALLED)" == "1" ]; then \
		$(ADB) uninstall abc.basiccalculator.test; \
	fi

# Phony  target
coverage-for-each-espresso-test : remove-apk-and-instrumented-tests app-and-instrumented-tests-for-coverage.apk $(ESPRESSO_TESTS_COVERAGE)
	@echo "Processed: $(shell echo $? | tr " " "\n")"
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators
	@echo "Done"


# Run existing unit tests (not carved ones)
coverage-unit-tests :
	$(GW) -PjacocoEnabled=true -PcarvedTests=false clean jacocoUnitTestCoverage 2>&1 | tee ${@}.log
	$(RM) -r unit-tests-coverage
	mv -v app/build/reports/jacoco/jacocoUnitTestCoverage unit-tests-coverage 

# UPDATE THIS WITH TO CHECK THE RIGHT FILES .covered or index.html.  --info
coverage-carved-tests : .carved-all
	$(GW) -PjacocoEnabled=true -PcarvedTests=true clean jacocoUnitTestCoverage 2>&1 | tee ${@}.log
	$(RM) -r carved-test-coverage
	mv -v build/carvedTest/coverage carved-test-coverage 

coverage-existing-carved-tests :
	$(GW) -PjacocoEnabled=true -PcarvedTests=true clean jacocoUnitTestCoverage  2>&1 | tee ${@}.log
	$(RM) -r  carved-test-coverage
	mv -v build/carvedTest/coverage carved-test-coverage