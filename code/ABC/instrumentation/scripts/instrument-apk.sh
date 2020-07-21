#!/bin/bash

# TODO I have no idea how I can make this configurable and ready for everybody
# This MUST BE PROVIDED somehow...
# APK_SIGNER="$HOME/Library/Android/sdk/build-tools/28.0.3/apksigner"

: ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}

set -e
trap 'catch $? $LINENO' EXIT
catch() {
  if [ "$1" != "0" ]; then
    # error handling goes here
    echo "Error $1 occurred on line $2"
  fi
}

export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)

# This script location
SCRIPT_LOCATION=$(dirname $(realpath $0))

# Predefined configurations
ANDROID_JAR=$(realpath "${SCRIPT_LOCATION}/../src/test/resources/android-28.jar")
OUTPUT_DIR=$(realpath "${SCRIPT_LOCATION}/../instrumented-apks")

# keystore created using keytool for signing instrumented APKs.
KEYSTORE=$(realpath "${SCRIPT_LOCATION}/file.keystore")
KEYALIAS=gambi

if [ $# -lt 1 ]; then 
    ( >&2 echo "** Missing parameter. Need an APK" )
    exit 1
fi

APK=$(realpath $1)

LOG_FILE=$(realpath ./instrumentation.log)

( >&2 echo "** Log output to ${LOG_FILE}" )

# Rebuild the project if necessary
if [ ! -e "${SCRIPT_LOCATION}/../target/appassembler" ]; then
    ( >&2 echo "** Build the project and assemble it" )
    
    pushd "${SCRIPT_LOCATION}/.." > /dev/null 2>&1
    mvn clean compile package appassembler:assemble -DskipTests >> ${LOG_FILE} 2>&1
    popd > /dev/null 2>&1
fi



( >&2 echo "** Instrumenting ${APK}" )
# Invoke the assembled script to instrument the APK. Do not tee on output

# Setup the Script option here
export JAVA_OPTS="-Dabc.output.instrumented.code"
( >&2 echo "** Java options for instrumentation are ${JAVA_OPTS}")

${SCRIPT_LOCATION}/../target/appassembler/bin/instrument-apk \
            --apk ${APK} \
            --android-jar ${ANDROID_JAR} \
            --output-to ${OUTPUT_DIR} > ${LOG_FILE} 2>&1

# For some weird reason the output is changed and an additional '.' was printed by the end of the name?
INSTRUMENTED_APK_FILE=$(cat ${LOG_FILE} | grep "Writing APK to" | awk '{print $NF}' | sed -e 's|"||' -e 's|".||g')

# Sign the APK ?
( >&2 echo "** Sign the apk ${INSTRUMENTED_APK_FILE} using ${APK_SIGNER}" | tee -a ${LOG_FILE} )

${APK_SIGNER} sign --ks ${KEYSTORE} --ks-key-alias ${KEYALIAS} --ks-pass pass:123456 --key-pass pass:123456 "${INSTRUMENTED_APK_FILE}" >> ${LOG_FILE} 2>&1

( >&2 echo "** Verify the signature" | tee -a ${LOG_FILE} )
jarsigner -verify -verbose -certs ${INSTRUMENTED_APK_FILE} >> ${LOG_FILE} 2>&1

# Enable further processing
echo "${INSTRUMENTED_APK_FILE}"
