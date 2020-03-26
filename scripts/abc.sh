#!/bin/bash
# Some utility scripts for the Action Based Carving (ABC) Framework

ABC_CONFIG="${ABC_CONFIG:-.abc-config}"

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

function __private_load_env(){
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

function start-clean-emulator(){
	# Ensures the required variables are in place
	: ${EMULATOR_EXE:?Please provide a value for EMULATOR_EXE in $config_file }
	: ${IMAGE_NAME:?Please provide a value for EMULATOR_EXE in $config_file }

	# Assume ONLY ONE emulator can be active at a time
	if [ $(ps aux | grep -c "$(dirname ${EMULATOR_EXE})") -gt 1 ]; then
		( >&2 echo "Emulator is already running")
		return
	fi

	# Run the command in background
	"${EMULATOR_EXE}" -avd ${IMAGE_NAME} -wipe-data &
}

function install-apk(){

	: ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file }

	local apk_file="${1:?Missing apk file to install}"

	start-clean-emulator

	${ANDROID_ADB_EXE} wait-for-device
	
	# We replace (-r) the app if it is already installed
    ${ANDROID_ADB_EXE} install -r ${apk_file}
}

function beautify(){
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
	local thread_name="${2:'UI:main'}"
	#
	local beautified_file="${trace_file}-beautified"
	( >&2 echo "Beautify ${trace_file} for thread ${thread_name} to ${beautified_file}")

	cat ${trace_file} | \
		sed 's|^ABC:: [0-9][0-9]* ||g' | \
		grep "\[${thread_name}" | \
		sed 's/\[.*\];\[/[/g' | \
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
			}{s=sprintf("%*s",level,""); gsub(/ /,"| ",s); printf("%s%-1s\n", s, $0)}' | \
		awk 'BEGIN {total=0} {if ($0 ~ /\[>/ ) {total++; printf "%-8d%-8s\n", total, $0} else { printf "%-8s%-8s\n", "", $0 }}' | \
		awk '{printf "%-8d%-8s\n", NR, $0}' > ${beautified_file}
}

function build_instrument(){
	# Ensures the required variables are in place
	: ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}

	# Store current folder in stack and cd to $ABC_HOME
	# NOTE: ABC_HOME should not be between double quotes (")
	pushd ${ABC_HOME}
	cd instrumentation
	mvn clean compile package appassembler:assemble -DskipTests
	# Return to original folder
	popd
}

function instrument_apk(){
	# Ensures the required variables are in place
	: ${ABC_HOME:?Please provide a value for ABC_HOME in $config_file}
	# This sets the env variable required by "instrument-apk.sh"
	: ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}

	local apk_file="${1:?Missing apk file to instrument}"

	# The instrumentation script also check if the project requires to be rebuild
	# TODO. Maybe we need to move that script here? Maybe we need to use make ?
	export APK_SIGNER=${APK_SIGNER}
	
	local instrumented_apk_file=$(${ABC_HOME}/instrumentation/scripts/instrument-apk.sh ${apk_file})
	# THIS PRODUCES A LOG "HERE". TODO Shall we move the log the location of the instrumented apk ?
	echo "Instrumented APK is ${instrumented_apk_file}"
}

function run_test(){
	# Ensures the required variables are in place
	: ${MONKEYRUNNER_EXE:?Please provide a value for MONKEYRUNNER_EXE in $config_file}
	: ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file } 

	# Mandatory. The file that contains instructions to be run with monkeyrunner
	local instructions_file="${1:?Missing an instructions file}"
	local apk="${2:?Missing an APK file}"

	# Application directory
	local apk_dir=${instructions_file%/*}
	# Reads the package name of the application
	local package_name=$(cat "$apk_dir"/.packagename)
	# Gets the path of the droixbench playback script
	local playback_script="$(dirname $(realpath $0))/../apks/automated-testing/monkey_playback.py"

	start-clean-emulator

	${ANDROID_ADB_EXE} wait-for-device

	# Watis until android booted completely
	booted=$(${ANDROID_ADB_EXE} shell getprop sys.boot_completed | tr -d '\r')
	while [ "$booted" != "1" ]; do
    	sleep 2
    	booted=$(${ANDROID_ADB_EXE} shell getprop sys.boot_completed | tr -d '\r')
	done

	# Checks if app is installed. Assumes that the directory contains exactly one apk file		
	if [ -z "$(${ANDROID_ADB_EXE} shell pm list packages $package_name)" ]; then 
		install-apk "$apk"
	fi

	${MONKEYRUNNER_EXE} "$playback_script" "$instructions_file" "$package_name" "$ANDROID_ADB_EXE" ./output > run_test.log 

	( >&2 echo "Test completed")

	copy-traces "$package_name"
}

function copy-traces(){
	# Ensures the required variables are in place
	: ${ANDROID_ADB_EXE:?Please provide a value for ANDROID_ADB_EXE in $config_file } 

	local package_name="${1:?Missing package name}"
	local output_dir="$ABC_HOME/carving/traces/$package_name"
	local tmp_dir="./tmp"

	mkdir -p "$tmp_dir"
	mkdir -p "$output_dir"

	# Restart daemon with root access in order to be able to access app data
	${ANDROID_ADB_EXE} root
	# Apparently, adb cannot copy files using wildcards, hence, we copy the whole package temporarily
	${ANDROID_ADB_EXE} pull "/data/data/$package_name" "$tmp_dir"

	# Iterate over trace files and copy them to the output dir
	for filename in "$tmp_dir"/"$package_name"/Trace-*.txt; do
  		file=$(basename "$filename")
  		( >&2 echo "Copying $filename")
  		cp "$filename" "$output_dir"/"$file"
	done

	# Remove temporary files
	rm -r "$tmp_dir"

	( >&2 echo "Traces were copied to $output_dir")
}

function edit_config(){
	nano ${ABC_CONFIG}
}

function show_config(){
	( >&2 echo "Config file contains:")
	( >&2 echo "-------------------")
	cat ${ABC_CONFIG}
}

function help(){
	# We output the message to std but the command to std out to enable autocompletion
	( >&2 echo "AVAILABLE COMMANDS")
	cat $0 | grep function | grep -v "__private" | grep -v "\#" | sed -e '/^ /d' -e 's|function \(.*\)(){|\1|g'
}

function __private_autocomplete(){
	local command_name=$1
	if [ "${command_name}" == "beautify" ]; then
		echo "requires_one_file"
	fi
	if [ "${command_name}" == "instrument_apk" ]; then
		echo "requires_one_file"
	fi
	if [ "${command_name}" == "run_test" ]; then
		# TODO actually requires two files
		echo "requires_one_file"
	fi
	if [ "${command_name}" == "install-apk" ]; then
		echo "requires_one_file"
	fi
	if [ "${command_name}" == "copy-traces" ]; then
		echo "requires_one_file"
	fi

}

# Always load the environment
__private_load_env

# Invoke functions by name
if declare -f "$1" > /dev/null
then
  # call arguments verbatim
  "$@"
else
  # Show a helpful error
  if [ $# -gt 0 ]; then
    (>&2 echo "'$1' is not a known function name") 
  fi

  help
#  exit 1
fi
