# Makefile

## Generating

The generator *make-makefile.py* exploits **jinja2** which needs to be installed (e.g., in a local python environment).

### Installing (Mac/Linux)

Go to the scripts folder and create a virtual env:

```python -m venv .venv```

Activate the .venv:

```. .venv/bin/activate```

Install the deps:

```pip install jinja2```

## How to run it

Assuming you have installed the required lib in the `script/.venv` folder, switch to the root folder of the app to analyze and type

`../../scripts/.venv/bin/python ../../scripts/make-makefile.py .`

Or invoke `abc.sh make .` from the root folder of the app to analyze.


## abc-apk-config

Each *abc-apk-config* file in the root directory of an evaluated application is used to generate a concrete makefile for that application and should have the following format:

```
[MakeConfiguration]
make_app_package = com.github.q115.goalie_android
make_app_debug_package = com.github.q115.goalie_android
// ...
```

## Available options

| Name  |  Description | Default value |
|---|---|---|
| `make_app_package`  | Original package name | - |
| `make_app_debug_package`  | Package name of the debug flavor | - |
| `make_original_apk_path`  | Path of the apk file when built in debug/free flavor (starting with app/build/...) | - |
| `make_android_test_apk_path`  | Path of the androidTest apk file | - |
| `make_assemble_apk_command`  | Gradle command used to build the apk | assembleDebug |
| `make_assemble_android_test_command`  | Gradle command used to build androidTest apk | assembleAndroidTest |
| `make_test_runner`  | JUnit runner name | - |
| `make_espresso_coverage_file_path`  | Original package name | - |
| `make_permissions`  | Comma-separated list of android permissions needed to run the espresso tests (e.g., SD card access) | - |
| `make_instr_skip_classes`  | Comma-separated list of classes to skip instrumentation of | - |
| `make_instr_filter_packages`  | Comma-separated list of packages to filter during instrumentation | - |
| `make_instr_filter_classes`  | Comma-separated list of classes to filter during instrumentation | - |
| `make_carving_filter_methods`  | Comma-separated list of methods to filter during carving | - |
| `make_trace_multiple_threads`  | Allow tracing multiple threads | false |
| `make_instr_filter_packages`  | Comma-separated list of packages to filter during instrumentation | - |
| `make_gui_coverage_file`  | Path to the jacoco coverage file (.ec) | app/build/outputs/code_coverage/debugAndroidTest/connected/*coverage.ec |





