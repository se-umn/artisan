#!/bin/bash

# TODO: Uniform all the variable names !!!

set -eE  # same as: `set -o errexit -o errtrace`
trap 'echo "Fail!"' ERR

# Input checking
if [ $# -lt 1 ];then
	echo "Usage: $0 apk-file"
	exit 1
else
	apkfile=$1
fi


# Read configuration input from provided `config.file`
if [ "$#" -lt 1 ]; then
    echo "You must provide a config file. Abort !"
    exit 1
else
    config_file=$1
	if [ "${config_file:0:1}" = "/" ]; then
		# absolute path
		echo "Absolute path"
    else
		# relative path. Make it absolute
		echo "Relative path"
		config_file=$(pwd)/$config_file
    fi
fi

if [ ! -f ${config_file} ]; then
    echo "Config file ${config_file} is missing. Abort !"
    exit 1
fi

echo "* Loading configuration from ${config_file}"

# Create a clean config file that avoids the quirks of using "~"
# Sed on mac is broken, so avoid "in place -i" substituion in favor of rewriting the file
sed "s|\~|${HOME}|g" "$config_file" > "${config_file}".tmp

# Load the properties from the clean file into ENV_VARIABLES
while read -r NAME VALUE; do
    declare $(echo ${NAME} | sed 's/\./_/g')="${VALUE}"
done < <(sed -e '/^$/d' -e '/^#/d' "${config_file}".tmp | sed 's|=| |')






### FINALLY CHECK THAT ALL THE VARIABLES ARE IN PLACE

# FAIL if any of the required configurations is missing

# TODO Uniform names of configurations ! 
: ${android_jar:?Please provide a value for android.jar in ${config_file}}
ANDROIDJAR=${android_jar}
# Before: ANDROIDJAR=$(cat ../cp.txt | tr ":" "\n" | grep android.jar)

# keystore created using keytool for signing instrumented APKs
: ${key_store:?Please provide a value for key.store in ${config_file}}
KEYSTORE=${key_store}
# Before: KEYSTORE=/Users/gambi/MyDroidFax/scripts/file.keystore

: ${key_alias:?Please provide a value for key.alias in ${config_file}}
KEYALIAS=${key_alias}
# Before KEYALIAS=gambi


: ${output_dir:?Please provide a value for output.dir in ${config_file}}
OUTDIR=${output_dir}
# Before: OUTDIR=cg.instrumented

# TODO Most likely we'll not get a DROIDFAX jar but something else !
# droidfax.jar Obtained from MVN like this mvn package
: ${droidfax_jar:?Please provide a value for droidfax.jar in ${config_file}}
# Before SOOTCP="droidfax.jar:$ANDROIDJAR"
SOOTCP="${droidfax_jar}:${ANDROIDJAR}"

# TODO This might be the CP needed to run the instrumentation code !
# Obtained from MVN like this
mvn -f ../pom.xml dependency:build-classpath -DincludeScope=compile -Dmdep.outputFile=cp.txt
MAINCP=$(cat ../cp.txt)":"$(echo $SOOTCP)

# get the apk file name without prefixing path and suffixing extension
suffix=${apkfile##*/}
suffix=${suffix%.*}

mkdir -p ${OUTDIR}
set -x

# -wrapTryCatch - TODO We might need to reintroduce this at some point...
#-instr3rdparty - TODO No idea what's this !

# Rebuild droidfax.jar which is linked here
mvn -f ../pom.xml clean package -DskipTests
# /ABC/local-repo/tmp/duafdroid/unkown/duafdroid-unkown.jar
java -Xmx14g -ea -cp ${MAINCP} de.unipassau.abc.instrumentation.SceneInstrumenterWithMethodParameters \
	-w -cp $SOOTCP -p cg verbose:false,implicit-entry:true \
	-p cg.spark verbose:false,on-fly-cg:true,rta:false \
	-d $OUTDIR \
	-noMonitorICC \
	-sclinit \
	-debug \
	-process-dir $apkfile

set +x

echo "now signing $suffix ..."
#echo "123456" | ./signandalign.sh $OUTDIR/${suffix}.apk 

echo "sign the apk ..."

set -x
#BEFORE: /Users/gambi/Library/Android/sdk/build-tools/28.0.3/apksigner
${apk_signer} \
 sign --ks ${KEYSTORE} \
	--ks-key-alias ${KEYALIAS} --ks-pass pass:123456 --key-pass pass:123456 \
	${OUTDIR}/${suffix}.apk 
set +x

echo "verify the signature just added ..."
jarsigner -verify -verbose -certs $apkfile
