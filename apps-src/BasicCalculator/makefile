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
# Create the list of expected coverage targets from tests.txt
ESPRESSO_TESTS_COVERAGE := $(shell cat tests.txt | sed '/^[[:space:]]*$$/d' | sed -e 's| |__|g' -e 's|^\(.*\)$$|espresso-test-coverage-for-\1/html/index.html|')

# Global variable to check which apks are installed, if any
TRACING_APKS_INSTALLED := 0
COVERAGE_APKS_INSTALLED := 0

# Ensure that if the emulator is running, there will be NO APKs installed afterwards
define remove-apk-and-instrumented-tests
$(info INFO: Removing APK and instrumented tests)
$(shell IS_RUNNING=$$(export ABC_CONFIG=$(ABC_CFG) && $(ABC) list-running-emulators | wc -l | sed 's| ||g'); \
if [ "$$IS_RUNNING" == "0" ]; then \
        (>&2 echo "No need to remove anything from emulator. No emulator running!"); \
elif [ "$$IS_RUNNING" == "1" ]; then \
        APP_INSTALLED=$$($(ADB) shell pm list packages | grep -c "abc.basiccalculator$$"); \
        INSTRUMENTATION_TESTS_INSTALLED=$$($(ADB) shell pm list packages | grep -c "abc.basiccalculator.test$$"); \
        if [ "$$APP_INSTALLED" == "1" ]; then \
				(>&2 echo "Removing app"); \
                $(ADB) uninstall abc.basiccalculator > /dev/null; \
        else \
                (>&2 echo "App not installed"); \
        fi; \
        if [ "$$INSTRUMENTATION_TESTS_INSTALLED" == "1" ]; then \
				(>&2 echo "Removing test app"); \
                $(ADB) uninstall abc.basiccalculator.test > /dev/null; \
        else \
                (>&2 echo "Test app not installed"); \
        fi; \
else \
    (>&2 echo "Too many emulators running at the same time: $(IS_RUNNING)"); \
fi)
$(eval TRACING_APKS_INSTALLED := 0)
$(eval COVERAGE_APKS_INSTALLED := 0)
endef

# Ensures that app-instrumented.apk and app-androidTest.apk are installed. Optionally, starts the emulator
define ensure_tracing_apks
$(info INFO: Ensure tracing APKs are installed [$(TRACING_APKS_INSTALLED)].)
$(eval IS_RUNNING := $(shell export ABC_CONFIG=$(ABC_CFG) && $(ABC) list-running-emulators | wc -l))
$(info INFO: Emulator running [$(IS_RUNNING)])
$(shell if [ "$(IS_RUNNING)" == "0" ]; then \
	(>&2 echo "No Emulator is running. Start a fresh one"); \
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator; \
fi)
$(if $(filter $(IS_RUNNING),0),$(eval TRACING_APKS_INSTALLED := 0),)
$(if $(filter $(IS_RUNNING),1),$(if $(filter $(TRACING_APKS_INSTALLED),0), $(call remove-apk-and-instrumented-tests),),)
$(shell if [ "$(TRACING_APKS_INSTALLED)" == "0" ]; then \
	(>&2 echo "Installing instrumented apk"); \
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-instrumented.apk; \
	(>&2 echo "Installing test apk") ;\
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-androidTest.apk; \
else \
	(>&2 echo "Resetting the data of the apk"); \
	$(ADB) shell pm clear abc.basiccalculator > /dev/null; \
fi)
$(eval TRACING_APKS_INSTALLED := 1)
endef

# Ensures that app-original-for-coverage.apk app-androidTest-for-coverage.apk are installed. Optionally, starts the emulator
define ensure_coverage_apks
$(info INFO: Ensure coverage APKs are installed [$(COVERAGE_APKS_INSTALLED)].)
$(eval IS_RUNNING := $(shell export ABC_CONFIG=$(ABC_CFG) && $(ABC) list-running-emulators | wc -l))
$(info INFO: Emulator running [$(IS_RUNNING)])
$(shell if [ "$(IS_RUNNING)" == "0" ]; then \
	(>&2 echo "No Emulator is running. Start a fresh one"); \
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) start-clean-emulator; \
fi)
$(if $(filter $(IS_RUNNING),0),$(eval COVERAGE_APKS_INSTALLED := 0),)
$(if $(filter $(IS_RUNNING),1),$(if $(filter $(COVERAGE_APKS_INSTALLED),0), $(call remove-apk-and-instrumented-tests),),)
$(shell if [ "$(COVERAGE_APKS_INSTALLED)" == "0" ]; then \
	(>&2 echo "Installing coverage apk"); \
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-original-for-coverage.apk; \
	(>&2 echo "Installing test coverage apk") ;\
	export ABC_CONFIG=$(ABC_CFG) && $(ABC) install-apk app-androidTest-for-coverage.apk; \
else \
	(>&2 echo "Resetting the data of the coverage apk"); \
	$(ADB) shell pm clear abc.basiccalculator > /dev/null; \
fi)
$(eval COVERAGE_APKS_INSTALLED := 1)
endef

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
	$(RM) -rv carved-tests-coverage

clean-espresso-coverage : 
	$(RM) -rv espresso-tests-coverage
	$(RM) -rv espresso-test-coverage-for-*
	$(RM) -rv jacoco-espresso-coverage

clean-all : clean-carved-tests clean-carved-coverage clean-espresso-coverage
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
	$(RM) -rv  unit-tests-coverage

# Build the various apks
app-original.apk : 
	@export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) -PjacocoEnabled=false assembleDebug && \
	mv ./app/build/outputs/apk/debug/app-debug.apk . && \
	$(ABC) sign-apk app-debug.apk && \
	mv -v app-debug.apk app-original.apk

app-instrumented.apk : app-original.apk
	@export ABC_CONFIG=$(ABC_CFG) && \
	export JAVA_OPTS=$(JAVA_OPTS) && \
	$(ABC) instrument-apk app-original.apk && \
	mv -v ../../code/ABC/instrumentation/instrumented-apks/app-original.apk app-instrumented.apk

app-androidTest.apk :
	@export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) assembleAndroidTest && \
	mv app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-androidTest-unsigned.apk && \
	$(ABC) sign-apk app-androidTest-unsigned.apk && \
	mv -v app-androidTest-unsigned.apk app-androidTest.apk

## the assembleAndroidTest task also builds the app if it starts from a clean build
app-original-for-coverage.apk app-androidTest-for-coverage.apk:
	@export ABC_CONFIG=$(ABC_CFG) && \
	$(GW) -PjacocoEnabled=true clean assembleDebug assembleAndroidTest && \
	mv ./app/build/outputs/apk/debug/app-debug.apk app-original-for-coverage.apk  && \
	mv ./app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk app-androidTest-for-coverage.apk

# Utility - TODO Maybe move this to function?
stop-emulator:
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators

# Trace all depends wraps all the tests to trace.
trace-all : $(ESPRESSO_TESTS)
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators
	@echo "Done"

$(ESPRESSO_TESTS) : app-instrumented.apk app-androidTest.apk 
# We need to ensure that when we trace the right apks are installed. This call is idempotent.
	$(call ensure_tracing_apks)
#	Evaluate the current test name. Note that test names use #
	$(eval TEST_NAME := $(shell echo "$(@)" | sed -e 's|__|\\\#|g' -e 's|.testlog||'))
	@echo "Tracing test $(TEST_NAME)"
#	Log directly to the expected file
	$(ADB) shell am instrument -w -e class $(TEST_NAME) abc.basiccalculator.test/androidx.test.runner.AndroidJUnitRunner 2>&1 | tee $(@)
#	Copy the traces if the previous command succeded
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) copy-traces abc.basiccalculator ./traces/$(TEST_NAME) force-clean

# This will always run because it's a PHONY target
carve-all : .carved-all
	@echo "Carving All"
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators
	@echo "Done"

# This requires to have all the tests traced
.carved-all : $(ESPRESSO_TESTS)
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) carve-all app-original.apk traces app/src/carvedTest force-clean 2>&1 | tee carving.log
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators
# 	Make sure this file has the right timestamp - probably touch will work the same
	@sleep 1; echo "" > .carved-all

run-carved-tests : carvedTests.log
	@echo "Done"

carvedTests.log : .carved-all
	@echo "Done"
	@touch carvedTests.log

### ### ### ### ### ### ### 
### Coverage targets
### ### ### ### ### ### ### 

coverage-espresso-tests : espresso-tests-coverage/html/index.html
	@echo "Done"

# We need to ensure the coverate apks because if the emulator is already running the target does not redeploy them
espresso-tests-coverage/html/index.html : app-original-for-coverage.apk app-androidTest-for-coverage.apk
# Ensure the right apk are installed and optionally start the emulator. Clean up the data if necessary.
	$(call ensure_coverage_apks)
	$(GW) -PjacocoEnabled=true -PcarvedTests=false clean jacocoGUITestCoverage 2>&1 | tee espresso-tests-coverage.log
	$(RM) -r espresso-tests-coverage
	mv -v app/build/reports/jacoco/jacocoGUITestCoverage espresso-tests-coverage
# This might not even be necessary
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators

coverage-for-each-espresso-test : $(ESPRESSO_TESTS_COVERAGE)
# This might not even be necessary
	@export ABC_CONFIG=$(ABC_CFG) && $(ABC) stop-all-emulators
	@echo "Done"

$(ESPRESSO_TESTS_COVERAGE): app-original-for-coverage.apk app-androidTest-for-coverage.apk
# Ensure the right apk are installed and optionally start the emulator. Clean up the data if necessary.
	$(call ensure_coverage_apks)
# Tack test/folder
	$(eval TEST_NAME := $(shell echo "$(@)" | sed -e 's|__|\\\#|g' -e 's|/html/index.html||' -e 's|espresso-test-coverage-for-||'))
	$(eval COVERAGE_FOLDER := $(shell echo "$(@)" | sed -e 's|/html/index.html||'))
# Execute the gradle target
	@echo "Running Test $(TEST_NAME)"
	$(GW) -PjacocoEnabled=true -PcarvedTests=false -Pandroid.testInstrumentationRunnerArguments.class=$(TEST_NAME) jacocoGUITestCoverage
	mv -v app/build/reports/jacoco/jacocoGUITestCoverage $(COVERAGE_FOLDER)
	mv -v app/build/outputs/code_coverage/debugAndroidTest/connected/*coverage.ec $(COVERAGE_FOLDER)/$(TEST_NAME).ec

# Run existing unit tests (not carved ones)
coverage-unit-tests : unit-tests-coverage/html/index.html
	@echo "Done"

unit-tests-coverage/html/index.html :
	$(GW) -PjacocoEnabled=true -PcarvedTests=false clean jacocoUnitTestCoverage 2>&1 | tee ${@}.log
	$(RM) -r unit-tests-coverage
	mv -v app/build/reports/jacoco/jacocoUnitTestCoverage unit-tests-coverage 

# UPDATE THIS WITH TO CHECK THE RIGHT FILES .covered or index.html.  --info
coverage-carved-tests : carved-tests-coverage/html/index.html
	@echo "Done"

carved-tests-coverage/html/index.html : .carved-all
	$(GW) -PjacocoEnabled=true -PcarvedTests=true clean jacocoUnitTestCoverage 2>&1 | tee ${@}.log
	$(RM) -r carved-tests-coverage
	mv -v build/carvedTest/coverage carved-tests-coverage