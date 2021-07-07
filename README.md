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
- To collect coverage, run:
    - For Espresso test coverage: `make coverage-espresso-tests`
    - For carved test coverage: `make coverage-carved-tests`

# Benchmark applications

| Name              | Tag/Commit                               | Fork                                         | .. |
|-------------------|------------------------------------------|----------------------------------------------|----|
| Omni-Notes        | b6f7990587d6a999ab6ebc89a2c49a6c75cdc73b | https://github.com/foxycom/Omni-Notes        |    |
| blabbertabber     | 1.0.10                                   | https://github.com/foxycom/blabbertabber     |    |
| moneywallet       | v4.0.5.9                                 | https://github.com/foxycom/moneywallet       |    |
| AnyMemo           | 10.11.6                                  | https://github.com/foxycom/AnyMemo           |    |
| PrismaCallBlocker | vn1.2.3_vc5                              | https://github.com/foxycom/PrismaCallBlocker |    |
| Kore              | v2.5.3                                   | https://github.com/foxycom/Kore              |    |
| Earthquake-Report | 6e26473394f4430b0dd82f47c1629974975f1f02 | https://github.com/foxycom/Earthquake-Report |    |
| gnucash-android   | v2.4.1                                   | https://github.com/foxycom/gnucash-android   |    |
| Debt-Manager      | 696886e3fed6152668e41d802c97dd49c36df1a9 | https://github.com/foxycom/Debt-Manager      |    |
| FifthElement      | d010a42884fd91f9cfcc62ff963a044b4156355f | https://github.com/foxycom/FifthElement      |    |
| Calculator        | v2.1                                     | https://github.com/foxycom/Calculator        |    |
| bird-monitor      | 2.3.1                                    | https://github.com/foxycom/filemanager       |    |
|                   |                                          |                                              |    |
|                   |                                          |                                              |    |
|                   |                                          |                                              |    |