#!/usr/bin/env bash
# Some utility scripts for the Action Based Carving (ABC) Framework

ABC_CONFIG="${ABC_CONFIG:-$(dirname "$0")/.abc-config}"


red=$(tput setaf 1)
green=$(tput setaf 2)
reset=$(tput sgr0)

VERBOSE=0
OPTIND=1
while getopts "v" opt; do
  case "$opt" in
  v)
    VERBOSE=1
    ;;
  esac
done

shift $((OPTIND - 1))

# OLD ALIASES/COMMANDS from Alessio

# ABC Framework
#alias reassemble='mvn clean package appassembler:assemble -DskipTests'
#alias repackage='mvn clean package -DskipTests'
#
#alias rebuild-trace='cd /Users/gambi/action-based-test-carving/code/ABC; mvn clean package -DskipTests -Ptrace; cd -'
#alias rebuild-testsubject='cd /Users/gambi/action-based-test-carving/code/ABC; mvn clean package -DskipTests -Ptestsubject; cd -'
#
#alias abc-system-tests="mvn test -Psystem-tests"
#alias abc-all-tests="mvn test; mvn test -Psystem-tests"
#
#alias reassemble-abc='cd /Users/gambi/action-based-test-carving/code/ABC; mvn clean package appassembler:assemble -DskipTests; cd -'
#

#
#abc-instrument-install() {
#   if [ "$#" -ne 1 ]; then
#      echo "Missing parameter apk.file.name";
#   else
#      # Try to unistall the package if any
#      # TODO Stop on Fail ?!
#      adb uninstall $(echo $1 | sed 's|_[0-9][0-9]*.*||')
#      rm /Users/gambi/MyDroidFax/scripts/cg.instrumented/$1
#      /Users/gambi/MyDroidFax/scripts/cgInstr.sh /Users/gambi/MyDroidFax/apks/$1 | tee instrumentation.log
#      adb install /Users/gambi/MyDroidFax/scripts/cg.instrumented/$1
#   fi
#}
#
##
# Use this when install fail for some reason but instrumentation was fine
#
#abc-logcat-now(){
#	#adb logcat -G 10M
#	# Print ONLY recent messages.
#	START_TIME="$(date +"%m-%d %H:%M:%S.000")"
#	echo "LogCat start at ${START_TIME}"
#	adb logcat -T "${START_TIME}" | tee full-trace.log
#}
#
#
#abc-debug-instrumentation() {
#	INSTRUMENTATION=${1:-instrumentation.log}
#        cat ${INSTRUMENTATION} | sed -n '/---- SceneInstrumenterWithMethodParameters.instrument() ----/,$p' > instrumentation-debug.log
#}
#

function __private_load_env() {
  if [ ! -f ${ABC_CONFIG} ]; then
    echo "Config file ${ABC_CONFIG} is missing. Abort"
    exit 1
  fi

  # Annoying message
  # ( >&2 echo "* Loading configuration from ${ABC_CONFIG}")

  # Avoids the quirks of using "~" and load global readonly properties from the configuration file
  while read -r NAME VALUE; do
    readonly $(echo ${NAME} | sed 's/\./_/g')="${VALUE}"
  done < <(sed -e '/^$/d' -e '/^#/d' -e "s|\~|${HOME}|g" "${ABC_CONFIG}" | sed 's|=| |')
}

function __log_verbose() {
  if [[ $VERBOSE -eq 1 ]]; then
    (echo >&2 "$@")
  fi
}

function start-clean-emulator() {
  # Ensures the required variables are in place
  : ${EMULATOR_EXE:?Please provide a value for EMULATOR_EXE in $config_file }
  : ${IMAGE_NAME:?Please provide a value for EMULATOR_EXE in $config_file }

  # Assume ONLY ONE emulator can be active at a time
  if [ $(ps aux | grep -c "$(dirname ${EMULATOR_EXE})") -gt 1 ]; then
    __log_verbose "Emulator is already running"
    disable-animations
    return
  fi

  # Also ensure there is only one adb emulator running
  running_devices=($(${ANDROID_ADB_EXE} devices | sed '1d'))
  for line in "${running_devices[@]}"; do
    [[ $line == emulator* ]] && __log_verbose "Emulator is running: $(echo $line | cut -f1)" && return
  done

  # Run the command in background
  "${EMULATOR_EXE}" -avd ${IMAGE_NAME} -wipe-data >/dev/null 2>&1 &

  ${ANDROID_ADB_EXE} wait-for-device >/dev/null 2>&1

  __log_verbose "Waiting until emulator has booted"
  # Waits until android booted completely
  booted=$(${ANDROID_ADB_EXE} shell getprop sys.boot_completed | tr -d '\r')
  while [ "$booted" != "1" ]; do
    sleep 2
    booted=$(${ANDROID_ADB_EXE} shell getprop sys.boot_completed | tr -d '\r')
  done

  disable-animations
}

function list-running-emulators() {
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }
  ${ANDROID_ADB_EXE} devices | grep emulator | cut -f1
}

function stop-all-emulators() {
  for emulator in $(list-running-emulators); do
    stop-emulator ${emulator}
  done
}

function stop-emulator() {
  # TODO This might be autocompleted with "list-running-emulators"
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }

  # Check that at least one is defined?
  local tmp=${1:?Missing emulator name. Run 'function list-running-emulators' to list the running emulators}
  unset tmp

  # TODO This can be improved
  local running_emulators=($(list-running-emulators))
  echo "Running emulators ${running_emulators[@]}?"
  for emulator_name in "$@"; do
    if [[ " ${running_emulators[@]} " =~ " ${emulator_name} " ]]; then
      # whatever you want to do when arr contains value
      echo "KILLING ${emulator_name}?"
      ${ANDROID_ADB_EXE} -s ${emulator_name} emu kill
    fi
  done
}

function __private_get_package_name_from_apk_file() {
  : ${ANDROID_AAPT_EXE:?Please provide a value for ANDROID_AAPT_EXE in $config_file }

  local apk_file="${1:?Missing apk file to install}"

  ${ANDROID_AAPT_EXE} dump badging ${apk_file} | grep "package: name" | awk '{print $2}' | sed -e "s|name=||" -e "s|'||g"
}

function __private_get_version_from_apk_file() {
  : ${ANDROID_AAPT_EXE:?Please provide a value for ANDROID_AAPT_EXE in $config_file }

  local apk_file="${1:?Missing apk file to install}"

  ${ANDROID_AAPT_EXE} dump badging ${apk_file} | grep "versionName=" | awk '{print $4}' | sed -e "s|versionName=||" -e "s|'||g" -e "s|\.||g"
}

function __private_get_application_label_from_apk_file() {
  : ${ANDROID_AAPT_EXE:?Please provide a value for ANDROID_AAPT_EXE in $config_file }

  local apk_file="${1:?Missing apk file to install}"

  ${ANDROID_AAPT_EXE} dump badging ${apk_file} | grep "application-label:" | awk '{print $1}' | sed -e "s|application-label:||" -e "s|'||g"
}

function disable-animations() {
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }

  # Disable animations
  ${ANDROID_ADB_EXE} shell settings put global window_animation_scale 0
  ${ANDROID_ADB_EXE} shell settings put global transition_animation_scale 0
  ${ANDROID_ADB_EXE} shell settings put global animator_duration_scale 0
}

function install-apk() {

  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }

  local apk_file="${1:?Missing apk file to install}"

  local package_name=$(__private_get_package_name_from_apk_file ${apk_file})

  start-clean-emulator

  if [ $(${ANDROID_ADB_EXE} shell pm list packages | grep -c "${package_name}") -gt 0 ]; then
    __log_verbose "App ${package_name} already installed. Unistall it"
    ${ANDROID_ADB_EXE} uninstall ${package_name} >/dev/null 2>&1
  fi

  if [[ $VERBOSE -eq 1 ]]; then
    (echo >&2 ${ANDROID_ADB_EXE} install ${apk_file})
  else
    ${ANDROID_ADB_EXE} install ${apk_file} >/dev/null 2>&1
  fi
}

function split-trace() {
  local trace_file="${1:?Missing trace file to beautify}"
  if [ ! -e ${trace_file} ]; then
    (echo >&2 "Trace file ${trace_file} does not exist.")
    return -1
  fi

  # TODO I cannot store strings with spaces into thread_names so I replace spaces with underscore... hoping this will not break anything...
  local thread_names=($(cat ${trace_file} | awk '{print $3,$4,$5}' | cut -f 1 -d';' | sort | uniq | tr " " "_"))

  for thread_name in "${thread_names[@]}"; do
    # Replace back the spaces
    thread_name="$(echo -e ${thread_name} | tr "_" " ")"
    __log_verbose "Splitting for ${thread_name}."
    # Replace NON-alfanumeric chars to _ to ensure a valid name is created for the file
    local file_pattern=$(echo -e "${thread_name}" | tr -c '[:alnum:]._-' '_')
    cat ${trace_file} | grep -F "${thread_name}" >"${trace_file}-split-${file_pattern}"

  done
}

function beautify() {
  #
  # This untility takes a trace, filters its entries by thread name using patter matching, and create a version of it which
  #	should be human-readable.
  # Lines are organized as follow:
  #
  # Method call # - Id of Method Call - Indentation level for nesting and easy matching
  # This might be improved using tput to enable colors: https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
  #
  #
  # Mandatory
  local trace_file="${1:?Missing trace file to beautify}"
  # Optional
  local thread_name="${2:-UI:main}"
  #
  local beautified_file="${trace_file}-beautified"
  (echo >&2 "Beautify ${trace_file} for thread ${thread_name} to ${beautified_file}")

  cat ${trace_file} |
    sed 's|^ABC:: [0-9][0-9]* ||g' |
    grep "\[${thread_name}" |
    sed 's/\[.*\];\[/[/g' |
    awk 'BEGIN{state=0; level=-1} \
			{ \
			if($0 ~ /\[>/ && state==0) { \
			state=0; level++; \
			} else if($0 ~ /\[>/ && state==1) { \
			state=0; \
			} else if($0 ~ /\[</ && state==0) { \
			state=1; \
			} else if($0 ~ /\[</ && state==1) { \
			state=1; level--;\
			} \
			}{s=sprintf("%*s",level,""); gsub(/ /,"| ",s); printf("%s%-1s\n", s, $0)}' |
    awk 'BEGIN {total=0} {if ($0 ~ /\[>/ ) {total++; printf "%-8d%-8s\n", total, $0} else { printf "%-8s%-8s\n", "", $0 }}' |
    awk '{printf "%-8d%-8s\n", NR, $0}' >${beautified_file}
}

function rebuild-all(){
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}

  # Store current folder in stack and cd to $ABC_HOME
  # NOTE: ABC_HOME should not be between double quotes (")
  pushd ${ABC_HOME}
  mvn clean compile package install -DskipTests 
  # Return to original folder

  pushd instrumentation
    mvn package appassembler:assemble -DskipTests 
  popd
    
  pushd synthesis
    mvn package appassembler:assemble -DskipTests 
  popd

  popd
}

function rebuild-instrument() {
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}

  # Store current folder in stack and cd to $ABC_HOME
  # NOTE: ABC_HOME should not be between double quotes (")
  pushd ${ABC_HOME}
  cd instrumentation
  mvn clean compile package install appassembler:assemble -DskipTests
  # Return to original folder
  popd
}

function rebuild-carving() {
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}

  # Store current folder in stack and cd to $ABC_HOME
  # NOTE: ABC_HOME should not be between double quotes (")
  pushd ${ABC_HOME}
  cd carving
  mvn clean compile package install appassembler:assemble -DskipTests
  # Return to original folder
  popd
}

function rebuild-synthesis() {
 # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}

  # Store current folder in stack and cd to $ABC_HOME
  # NOTE: ABC_HOME should not be between double quotes (")
  pushd ${ABC_HOME}

  cd synthesis
    mvn package appassembler:assemble -DskipTests 
  popd
}


function sign-apk() {
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  
  local apk_file="${1:?Missing apk file to instrument}"

  # The instrumentation script also check if the project requires to be rebuild
  # TODO. Maybe we need to move that script here? Maybe we need to use make ?
  export APK_SIGNER=${APK_SIGNER}
  
  local signed_apk_file=$(${ABC_HOME}/instrumentation/scripts/sign-apk.sh ${apk_file})

  (echo >&2 "** Signed APK is:")
  echo "${signed_apk_file}"
}

function instrument-apk() {
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${ANDROID_JAR:?Please provide a value for ANDROID_JAR in $config_file}

  local apk_file="${1:?Missing apk file to instrument}"

  # Extract the package from apk and set it as global variable
  # such that the instrumenter includes it in the traces
  set-java-opts "${apk_file}"

  # The instrumentation script also check if the project requires to be rebuild
  # TODO. Maybe we need to move that script here? Maybe we need to use make ?
  export APK_SIGNER=${APK_SIGNER}
  export ANDROID_JAR=${ANDROID_JAR}
  if [ ! -z "${INSTRUMENTATION_OPTS}" ]; then
    (echo >&2 "** Exporting INSTRUMENTATION_OPTS: " ${INSTRUMENTATION_OPTS})
    export INSTRUMENTATION_OPTS=${INSTRUMENTATION_OPTS}
  fi

  local instrumented_apk_file=$(${ABC_HOME}/instrumentation/scripts/instrument-apk.sh ${apk_file})
  # THIS PRODUCES A LOG "HERE". TODO Shall we move the log the location of the instrumented apk ?


  (echo >&2 "** Instrumented APK is: ${instrumented_apk_file}")
  echo "${instrumented_apk_file}"
}

function carve-and-generate-from-trace() {
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${ANDROID_JAR:?Please provide a value for ANDROID_JAR in $config_file}

  local apk_file="${1:?Missing apk file}"
  local trace_file="${2:?Missing trace file}"
  local output_to="${3:?Missing output folder}"

  ${ABC_HOME}/synthesis/target/appassembler/bin/carve-and-generate --android-jar=${ANDROID_JAR} \
      --trace-files=${trace_file} \
      --apk=${apk_file} \
      --output-to=${output_to}
  # Does this produce a log "HERE" ?  
}

function carve-one(){
    # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${ANDROID_JAR:?Please provide a value for ANDROID_JAR in $config_file}

  local apk_file="${1:?Missing apk file}"
  local trace_folder="${2:?Missing trace folder}"
  local output_dir="${3:?Missing output folder}"

  if [ -z "$4" ]
  then
    (echo >&2 "Do not clean existing carved tests folder")
  else
    (echo >&2 "Clean existing carved tests folder")
    if [ -e $output_dir ]; then rm -rfv $output_dir; fi
  fi

  mkdir -p ${output_dir}
  
  # Build a string with all the trace files
  trace_files=$(find traces -type f | tr "\n" " ")
  
  ${ABC_HOME}/synthesis/target/appassembler/bin/carve-and-generate --android-jar=${ANDROID_JAR} \
      --trace-files=${trace_files} \
      --apk=${apk_file} \
      --output-to=${output_dir} \
      --selection-strategy=SELECT_ONE
}

function carve-all(){
    # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}
  # This sets the env variable required by "instrument-apk.sh"
  : ${ANDROID_JAR:?Please provide a value for ANDROID_JAR in $config_file}

  local apk_file="${1:?Missing apk file}"
  local trace_folder="${2:?Missing trace folder}"
  local output_dir="${3:?Missing output folder}"

  if [ -z "$4" ]
  then
    (echo >&2 "Do not clean existing carved tests folder")
  else
    (echo >&2 "Clean existing carved tests folder")
    if [ -e $output_dir ]; then rm -rfv $output_dir; fi
  fi

  mkdir -p ${output_dir}
  
  # Build a string with all the trace files
  trace_files=$(find traces -type f | tr "\n" " ")
  
  if [ ! -z "${CARVING_OPTIONS}" ]; then
    (echo >&2 "** Activating CARVING_OPTIONS: " ${CARVING_OPTIONS})
  fi

  if [ ! -z "${CARVING_JAVA_OPTIONS}" ]; then
    (echo >&2 "** Activating CARVING_JAVA_OPTIONS: " ${CARVING_JAVA_OPTIONS})
    # TODO Not sure I need to use the "
    export JAVA_OPTS="${JAVA_OPTS} ${CARVING_JAVA_OPTIONS}"
  fi
  
  (echo >&2 "** JAVA_OPTS: " ${JAVA_OPTS})

  ${ABC_HOME}/synthesis/target/appassembler/bin/carve-and-generate --android-jar=${ANDROID_JAR} \
      --trace-files=${trace_files} \
      --apk=${apk_file} \
      --output-to=${output_dir} \
      ${CARVING_OPTIONS}

  # Carve all of them, one by one
  # carve-and-generate-from-trace ${apk_file} ${trace_files} ${output_dir}
  # for trace_file in $(find ${trace_folder} -iname "Trace*.txt"); do
  # test_name=$(echo -e $(basename ${trace_file}) | sed -e 's|Trace-\(.*\)-[1-9].*.txt|\1|')
  # (echo >&2 "Start carving tests from ${trace_file} for test ${test_name}")  
  # done
}

function copy-traces() {
  # Ensures the required variables are in place
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }

  local package_name="${1:?Missing package name}"
  # TODO ALESSIO: I do not really like this but leave it be for the moment
  local output_dir="${2:-$ABC_HOME/carving/traces/$package_name}"
  local force_clean=$3

  if [ -z "$3" ]
  then
    (echo >&2 "Do not clean existing trace folder")
  else
    (echo >&2 "Clean existing trace folder")
    if [ -e $output_dir ]; then rm -rfv $output_dir; fi
  fi
  mkdir -p "$output_dir"

  local tmp_dir="$(mktemp -d)"

  #copy folder to a location that can be pulled
  ${ANDROID_ADB_EXE} shell run-as "$package_name" cp -r "/data/data/$package_name" /sdcard
  # Apparently, adb cannot copy files using wildcards, hence, we copy the whole package temporarily
  ${ANDROID_ADB_EXE} pull "/sdcard/$package_name" "$tmp_dir"

  # Iterate over trace files and copy them to the output dir
  for filename in "$tmp_dir"/"$package_name"/Trace-*.txt; do
    file=$(basename "$filename")
    local output_trace="${output_dir}/${file}"

    __log_verbose "Copying $filename to ${output_trace}"
    cp "$filename" "${output_trace}"
    # This is necessary because other functions use this output
    echo "${output_trace}"
  done

  # Remove temporary files
  rm -r "$tmp_dir"

  #copy folder to a location that can be pulled
  ${ANDROID_ADB_EXE} shell run-as "$package_name" rm -r "/sdcard/$package_name"

  (echo >&2 "Done Copying")
}

function run-test() {
  # Ensures the required variables are in place
  : ${MONKEYRUNNER_EXE:?Please provide a value for MONKEYRUNNER_EXE in $config_file}
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }

  # Mandatory. The file that contains instructions to be run with monkeyrunner
  local apk_file="${1:?Missing an APK file}"

  if [ ! -e ${apk_file} ]; then
    (echo >&2 "The provided APK ${apk_file} does not exist.")
    return -1
  fi

  # Application directory
  local apk_dir=${apk_file%/*}

  # Reads the package name of the application
  local package_name=$(__private_get_package_name_from_apk_file ${apk_file})

  start-clean-emulator

  # Checks if app is installed. Assumes that the directory contains exactly one apk file
  (echo >&2 "(Re)Installing the APK")
  install-apk "$apk_file"

  local tests_dir=$(realpath "${ABC_HOME}/../../apps-src/tests")
  pushd ${tests_dir} &> /dev/null || ((echo >&2 "Tests directory $tests_dir does not exist") && exit)

  # Build the tests
  local build_result=$("$tests_dir/gradlew" assembleAndroidTest)

  # Both apks must be installed on the device for the tests to work properly
  local androidTestDebugApk=$(realpath "$tests_dir/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk")
  local debugApk=$(realpath "$tests_dir/app/build/outputs/apk/debug/app-debug.apk")
  popd &> /dev/null || exit

  # The "testing" apk must be installed, afterwards it can be invoked to test apk under test
  # Check installed instrumentation with 'adb shell pm list instrumentation'
  install-apk $androidTestDebugApk
  install-apk $debugApk

  # Combine the test class name out of application name and version assuming they are correctly defined.
  # Other options are:
  # 1) Create a hard-written list of apk-specific test methods / classes (adb supports such a list directly)
  # 2) Create some sort of marker annotation, which means creating an annotation for every single apk
  # https://developer.android.com/reference/androidx/test/runner/AndroidJUnitRunner
  local apk_version=$(__private_get_version_from_apk_file ${apk_file})
  local apk_name=$(__private_get_application_label_from_apk_file ${apk_file})

  __log_verbose "APK name is ${apk_name}, version: ${apk_version}"

  local test_name="de.unipassau.tests.${apk_name}${apk_version}Test"
  if [ ! -z "$2" ]; then
    # Run only one test method
    test_name="$test_name#$2"
  fi

  # Run specific test class/method that contains UI tests for the apk under test.
  # UIautomator test log contains each test method twice
  local test_methods=$(${ANDROID_ADB_EXE} shell am instrument \
      -w -r -e debug false -e class ${test_name} \
      -e apk-path ${apk_file} \
      de.unipassau.tests.test/androidx.test.runner.AndroidJUnitRunner | grep 'test=' \
      | awk 'NR % 2 == 0 {print $2}' | sed -e 's/test=//g')

  (echo >&2 "Test(s) completed")
  local traces=$(copy-traces "$package_name" | sort)

  IFS=$'\n'
  read -rd '' -a trace_paths <<< "$traces"
  read -rd '' -a test_methods <<< "$test_methods"

  for i in "${!trace_paths[@]}"; do
    echo "${test_methods[${i}]}:${trace_paths[${i}]}"
  done
}

function test-apk() {
  # Ensures the required variables are in place
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }
  local ORIGINAL_APK="${1:?Missing APK}"
  local INSTRUMENTED_APK=$(instrument-apk ${ORIGINAL_APK})

  if [ -z "$INSTRUMENTED_APK" ]; then
    ( >&2 echo "${red}INSTRUMENTATION FAILED${reset}" ) && exit
  fi

  local apk_dir=$(dirname "$ORIGINAL_APK")
  pushd $apk_dir || exit
  # Clean up
  make &> /dev/null
  popd

  for entry in $(run-test $INSTRUMENTED_APK); do
    local method_name=$(echo $entry | cut -d ':' -f 1)
    local TRACE=$(echo $entry | cut -d ':' -f 2)
    local trace_path=$(echo "$TRACE" | sed -e "s/Trace-/Trace-${method_name}-/g")
    echo "Test name: ${green}${method_name}${reset}"
    # Copy the trace in the test folder using the test name as template
    cp -v "${TRACE}" "${apk_dir}/$(basename ${trace_path})-trace"

    split-trace "$TRACE" &>/dev/null

    for SPLIT in "$TRACE"-split*; do
      # Actually check that the number of lines in the trace are even
      local number_of_line=$(wc -l ${SPLIT} | awk '{print $1}')
      if [ ! $((number_of_line % 2)) -eq 0 ]; then
        # https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
        thread_name=$(echo "$SPLIT" | awk 'match($0, /(split\-)[a-zA-Z 0-9 \- _]+$/) {print substr($0, RSTART + 7, RLENGTH)}' | tr "_" " ")
        VERDICT="${red}FAIL${reset}: expected an even number of lines in thread $thread_name but got ${number_of_line}"
        break
      else
        VERDICT="${green}PASS${reset}"
      fi
    done
    (echo >&2 "${VERDICT}")
  done
}

function test-all-apks() {
  : ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }
  : ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}

  local apks_dir=$(realpath $ABC_HOME/../../apks/automated-testing)
  for d in $apks_dir/*; do
    local apks_in_d=($(find $d -maxdepth 1 -type f -iname "*.apk"))
    if [ ${#apks_in_d[@]} -gt 0 ]; then
      (echo >&2 "Testing ${green}${apks_in_d[0]}${reset}")

      # Let's assume there is only one APK per directory
      test-apk ${apks_in_d[0]} 2>&1 | sed 's/^/  /'
    fi
  done
}

function parse() {
  local trace_file=${1:?Missing trace file}
  local stack=()
  local size=0

  line_n=1
  while IFS="" read -r line || [ -n "$line" ]; do
    #( >&2 printf "%s\n" "$line")
    IFS=';' read -r -a items <<<"$line"
    local thread=$(echo ${items[0]} | awk 'match($0, /\[UI:main\]|\[AsyncTask #[0-9]+\]/) {print substr($0,RSTART,RLENGTH)}')
    local token="${items[2]}"
    local method="${items[4]}"
    local identificator="$thread $method"

    if [[ "$token" == *">"* ]]; then
      # Method entry
      stack+=("$identificator")
      ((size++))
    elif [[ "$token" == *"<"* ]]; then
      # Method return

      # Iterate over the stack to find the last called method on this thread
      ok=1
      i=$(expr $size - 1)

      while [[ i -ge 0 ]]; do
        if [[ -z ${stack[i]} ]]; then
          # Skip gap
          ((i--))
          continue
        fi

        local entry=${stack[i]}
        if [[ $entry == "$thread"* ]]; then
          # Correct thread, check method signature
          if [[ "$entry" == "$identificator" ]]; then
            # Remove element from array
            unset 'stack[i]'
            ok=0
            break
          else
            # Method signatures do not match
            (printf >&2 "Parsing error:\n\tUnexpected method return in line %d:\n\tExpected %s, but got %s\n" "$line_n" "$entry" "$identificator")
            return 1
          fi
        else
          # Thread mismatch
          ((i--))
          continue
        fi
      done

      if [[ ok -eq 1 ]]; then
        (printf >&2 "Parsing error:\n\tUnexpected method return in line %d:\n\tGot %s, but the stack is empty\n" "$line_n" "$identificator")
        return 1
      fi
    fi
    ((line_n++))
  done <"$trace_file"
  return 0
}

function edit-config() {
  nano ${ABC_CONFIG}
}

function edit-abc() {
  # This does not work
  # local editor=${ABC_EDITOR:-nano}
  # ${editor} $0
  # TODO Temporary trick as I registered alredy my editor
  open -W ${0}
}

function show-config() {
  # (echo >&2 "-------------------")
  # (echo >&2 "Config file contains:")
  # (echo >&2 "-------------------")
  if [ $# == 0 ]; then
    cat ${ABC_CONFIG}
  elif [ $# == 1 ]; then
    cat ${ABC_CONFIG} | grep $1
  else 
    cat ${ABC_CONFIG}
  fi
}

# shellcheck disable=SC2120
function make() {
  MAKE_GENERATOR="${MAKE_GENERATOR:-$(dirname "$0")/make-makefile.py}"

  local app_root="${1:?Missing application root directory}"
  local absolute_app_root=$(realpath "${app_root}")
  #(echo >&2 "Application root: $app_root")

  if [[ -f "${app_root}/abc-apk-config" ]]; then
    python3 "${MAKE_GENERATOR}" "${absolute_app_root}"
  else
    (echo >&2 "$app_root/abc-apk-config does not exist")
    return 1
  fi
}

function set-java-opts() {
	apk="$1"
	if [[ ! -f "$apk" ]]; then
		(printf >&2 "Apk %s does not exist\n" "$apk")
		exit 1
	fi
	package_name=$(__private_get_package_name_from_apk_file "$apk")
  # The package name sometimes is computed dynamically....
	# export JAVA_OPTS="$JAVA_OPTS -Dabc.instrument.include=$package_name"
  export JAVA_OPTS="$JAVA_OPTS"
}

function help() {
  # We output the message to std but the command to std out to enable autocompletion
  (echo >&2 "AVAILABLE COMMANDS...")
  cat $0 | grep function | grep -v "__" | grep -v "\#" | sed -e '/^ /d' -e 's|^function ||' -e 's|^\(.*\)().*|\1|g'
}

function __private_autocomplete() {
  local command_name=$1
  if [ "${command_name}" == "beautify" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "instrument-apk" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "run-test" ]; then
    # TODO actually requires two files
    echo "requires_one_file"
  elif [ "${command_name}" == "install-apk" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "sign-apk" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "copy-traces" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "split-trace" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "test-apk" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "carve-and-generate-from-trace" ]; then
    echo "requires_one_file"
  elif [ "${command_name}" == "carve-all" ]; then
    echo "requires_one_file"
  fi
}

# Always load the environment
__private_load_env

# Invoke functions by name
if declare -f "$1" >/dev/null; then
  # call arguments verbatim
  "$@"
else
  # Show a helpful error
  if [ $# -gt 0 ]; then
    (echo >&2 "'$1' is not a known function name")
  fi

  help
#  exit 1
fi
