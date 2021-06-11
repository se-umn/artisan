# Pre-build instructions:

## .abc-config

- Ensure the contents of `./scripts/.abc-config` look something like the below; this will vary by machine:

```
EMULATOR_EXE=~/Android/Sdk/emulator/emulator
IMAGE_NAME=Pixel_3_API_28
ABC_HOME=~/action-based-test-carving-verify-36/code/ABC
APK_SIGNER=~/Android/Sdk/build-tools/29.0.3/apksigner
MONKEYRUNNER_EXE=~/Android/Sdk/tools/bin/monkeyrunner
ANDROID_ADB_EXE=~/Android/Sdk/platform-tools/adb
ANDROID_AAPT_EXE=~/Android/Sdk/build-tools/29.0.3/aapt
ANDROID_JAR=~/Android/Sdk/platforms/android-29/android.jar
```

## Environment variables:

- Append `./scripts/` to your `$PATH`; this may be done on POSIX shells as below:

`export PATH=<absolute-path-to-action-based-test-carving-directory>/scripts/:$PATH`

- Append a directory containing `adb` to your `$PATH`; this may be `~/Android/Sdk/platform-tools/`:

`export PATH=~/Android/Sdk/platform-tools:$PATH`

- Set `$ANDROID_HOME`; this is usually `~/Android/Sdk`:

`export ANDROID_HOME=~/Android/Sdk/`

# Build instructions:

- Run `abc.sh rebuild-all`; this should build all necessary components of the project.

# Running instructions:

## BasicCalculator:

- To carve tests, change directory to `./apps-src/BasicCalculator` and run `make carve-all`
- To run the carved tests, run `make run-all-carved-tests`
- To carve tests from traces in the `traces` folder run `make carve-cached-traces` 
- To collect coverage, run:
    - For Espresso test coverage: `make coverage-espresso-tests`
    - For carved test coverage: `make coverage-carved-tests`

