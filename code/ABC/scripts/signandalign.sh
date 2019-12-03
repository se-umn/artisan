#!/bin/bash

# keystore created using keytool for signing instrumented APKs
KEYSTORE=file.keystore
KEYALIAS=gambi


if [ $# -lt 1 ];
then
	echo "usage: $0 apkfile"
	exit 0
fi
apkfile=$1
OUTDIR=cg.instrumented

echo "sign the apk ..."

set -x
/Users/gambi/Library/Android/sdk/build-tools/28.0.3/apksigner sign --ks ${KEYSTORE} \
	--ks-key-alias ${KEYALIAS} --ks-pass pass:123456 --key-pass pass:123456 \
	${apkfile} 
set +x

#jarsigner -verbose \
#		-sigalg SHA1withRSA \
#		-digestalg SHA1 \
#		-keystore $KEYSTORE \
#		$apkfile \
#        $KEYALIAS

echo "verify the signature just added ..."
jarsigner -verify -verbose -certs $apkfile

exit 0

# Unreacheable code

echo "align the signed APK package ..."
outfn=${apkfile%.*}_signed.apk
if [ -s $outfn ];
then
	echo "remove existing version - $outfn"
	rm -f ${outfn}
fi
zipalign -v 4 $apkfile $outfn

echo "finished signing and aligning successfully."
