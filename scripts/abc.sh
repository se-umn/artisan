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
#abc-instrument() {
#   if [ "$#" -ne 1 ]; then
#      echo "Missing parameter apk.file.name";
#   else
#      /Users/gambi/MyDroidFax/scripts/cgInstr.sh /Users/gambi/MyDroidFax/apks/$1 | tee instrumentation.log
#   fi
#}
#
#
# Use this when install fail for some reason but instrumentation was fine
#abc-install() {
#   if [ "$#" -ne 1 ]; then
#      echo "Missing parameter apk.file.name";
#   else
#      adb install /Users/gambi/MyDroidFax/scripts/cg.instrumented/$1
#   fi
#}
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

	echo "* Loading configuration from ${ABC_CONFIG}"

	# Avoids the quirks of using "~" and load global readonly properties from the configuration file
	while read -r NAME VALUE; do
    	readonly $(echo ${NAME} | sed 's/\./_/g')="${VALUE}"
	done < <(sed -e '/^$/d' -e '/^#/d' -e "s|\~|${HOME}|g" "${ABC_CONFIG}" | sed 's|=| |')
}

function start-clean-emulator(){
	__private_load_env

	# Ensures the required variables are in place
	: ${EMULATOR_EXE:?Please provide a value for EMULATOR_EXE in $config_file }
	: ${IMAGE_NAME:?Please provide a value for EMULATOR_EXE in $config_file }

	# Run the command
	"${EMULATOR_EXE}" -avd "${IMAGE_NAME}" -wipe-data
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


function help(){
	echo "AVAILABLE COMMANDS"
	cat abc.sh | grep function | grep -v "__private" | grep -v "\#" | sed -e '/^ /d' -e 's|function \(.*\)(){|\1|g'
}

# Invoke functions by name
if declare -f "$1" > /dev/null
then
  #
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
