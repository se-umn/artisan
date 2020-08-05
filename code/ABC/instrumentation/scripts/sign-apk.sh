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

export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)

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
${APK_SIGNER} sign --ks ${KEYSTORE} --ks-key-alias ${KEYALIAS} --ks-pass pass:123456 --key-pass pass:123456 "${APK}" > /dev/null 2>&1

( >&2 echo "** Verify the signature")
## Note jarsigner is on the path...
jarsigner -verify -verbose -certs ${APK} > /dev/null 2>&1

# Enable further processing
echo "${APK}"
