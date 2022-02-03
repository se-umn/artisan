#!/bin/bash

# This is a copy and cut of instrument.sh
: ${APK_SIGNER:?Please provide a value for APK_SIGNER in $config_file}

set -e
trap 'catch $? $LINENO' EXIT
catch() {
  if [ "$1" != "0" ]; then
    # error handling goes here
    echo "Error $1 occurred on line $2"
  fi
}

# Ensure Java is set nad it's 1.8
# https://stackoverflow.com/questions/7334754/correct-way-to-check-java-version-from-bash-script
if [[ -z ${JAVA_HOME} ]]; then
  export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
fi
JAVA_VER=$(${JAVA_HOME}/bin/java -version 2>&1 | sed -n ';s/.* version "\(.*\)\.\(.*\)\..*".*/\1\2/p;')
( >&2 echo "** Current Java version (as per JAVA_HOME). ${JAVA_VER}" )

if [[ "$JAVA_VER" -eq 18 ]]; then
  ( >&2 echo "** Java Version OK" )
else
  ( >&2 echo "** WRONG Java Version. Exit with error" )
  exit 1
fi

# This script location
SCRIPT_LOCATION=$(dirname $(realpath $0))

# keystore created using keytool for signing instrumented APKs.
KEYSTORE=$(realpath "${SCRIPT_LOCATION}/file.keystore")
KEYALIAS=gambi

if [ $# -lt 1 ]; then 
    ( >&2 echo "** Missing parameter. Need an APK" )
    exit 1
fi

APK=$(realpath $1)

# Sign the APK ?
( >&2 echo "** Sign the apk ${APK} using ${APK_SIGNER}")

# TODO This will produce some phony output - redirect everything to stderr
${APK_SIGNER} sign --ks ${KEYSTORE} --ks-key-alias ${KEYALIAS} --ks-pass pass:123456 --key-pass pass:123456 "${APK}" 2>&1 > /dev/null 

( >&2 echo "** Verify the signature")
## Note jarsigner is on the path...
jarsigner -verify -verbose -certs ${APK} 2>&1 > /dev/null

# Enable further processing
echo "${APK}"
