#!/bin/bash
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)

# Predefined configurations
ANDROID_JAR=$(realpath "../src/test/resources/android-28.jar")
OUTPUT_DIR=$(realpath "../instrumented-apks")

# keystore created using keytool for signing instrumented APKs.
KEYSTORE=$(realpath "./file.keystore")
KEYALIAS=gambi

# TODO I have no idea how I can make this configurable and ready for everybody
APK_SIGNER="$HOME/Library/Android/sdk/build-tools/28.0.3/apksigner"

if [ $# -lt 1 ]; then 
    echo "Missing parameter. Need an APK";
    exit 1
fi

APK=$(realpath $1)

# Rebuild the project if necessary
if [ ! -e "../target/appassembler/" ]; then
    echo "Build the project and assemble it"
    mvn clean compile package appassembler:assemble -DskipTests
fi

# Invoke the script to instrument the APK
../target/appassembler/bin/instrument-apk \
            --apk ${APK} \
            --android-jar ${ANDROID_JAR} \
            --output-to ${OUTPUT_DIR} | tee instrumentation.log

INSTRUMENTED_APK_FILE=$(cat instrumentation.log | grep "Writing APK to" | awk '{print $NF}')

# Sign the APK ?
echo "Sign the apk ${INSTRUMENTED_APK_FILE} using ${APK_SIGNER}"

${APK_SIGNER} sign --ks ${KEYSTORE} --ks-key-alias ${KEYALIAS} --ks-pass pass:123456 --key-pass pass:123456 ${INSTRUMENTED_APK_FILE} 

echo "Verify the signature"
jarsigner -verify -verbose -certs ${INSTRUMENTED_APK_FILE}