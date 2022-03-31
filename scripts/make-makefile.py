import os
import re
import sys
from string import Template
import configparser

script_path = os.path.realpath(__file__)
scripts_dir = os.path.dirname(script_path)
template_path = os.path.join(scripts_dir, "make-template.make")

def parse_java_opts(config_values):
    trace_multiple_threads = config_values["make_trace_multiple_threads"]
    if "true" in trace_multiple_threads:
        config_values["make_trace_multiple_threads"] = " -Dabc.instrument.multithreaded "
    else:
        config_values["make_trace_multiple_threads"] = ""

def parse_permissions(config_values):
  permissions = config_values["make_permissions"]
  if permissions:
    permissions_list = [f"$(ADB) shell pm grant {config_values['make_app_debug_package']} {p};\\\n" for p in permissions.split(",")]
    config_values["permissions"] = "".join(permissions_list)
  else:
    config_values["permissions"] = ""

def parse_carving_options(config_values):
    filter_methods = config_values["make_carving_filter_methods"]
    if filter_methods:
        filter_method_list = [" \\"] + [f"--filter-method {method} \\" for method in re.split("\s*,\s*", filter_methods)] + [""]
        config_values["make_carving_filter_methods"] = "\n".join(filter_method_list)
    else:
        config_values["make_carving_filter_methods"] = ""

def parse_instrumentation_options(config_values):
    skip_classes = config_values["make_instr_skip_classes"]
    if skip_classes:
        classes_list = [f"--skip-class={clazz} \\" for clazz in re.split("\s*,\s*", skip_classes)]
        config_values["make_instr_skip_classes"] = "\n".join(classes_list)
    else:
        config_values["make_instr_skip_classes"] = "\\"

    filter_packages = config_values["make_instr_filter_packages"]
    if filter_packages:
        packages_list = [f"--filter-package={package} \\" for package in re.split("\s*,\s*", filter_packages)]
        config_values["make_instr_filter_packages"] = "\n".join(packages_list)
    else:
        config_values["make_instr_filter_packages"] = "\\"

    filter_classes = config_values["make_instr_filter_classes"]
    if filter_classes:
        classes_list = [f"--filter-class={clazz} \\" for clazz in re.split("\s*,\s*", filter_classes)]
        config_values["make_instr_filter_classes"] = "\n".join(classes_list)
    else:
        config_values["make_instr_filter_classes"] = "\\"

if len(sys.argv) == 1:
    print("Missing application root directory")
else:
    config_path = os.path.join(sys.argv[1], "abc-apk-config")
    if not os.path.isdir(sys.argv[1]) or not os.path.isfile(config_path):
        print(f"{config_path} does not exist")
    else:
        config = configparser.ConfigParser()
        config.read(config_path)
        config_values = config["MakeConfiguration"]
        parse_java_opts(config_values)
        parse_permissions(config_values)
        parse_carving_options(config_values)
        parse_instrumentation_options(config_values)
        with open(template_path, "r") as template_file:
            template = Template(template_file.read())
            output = template.substitute(config_values)
            make_file_path = os.path.join(sys.argv[1], "makefile")
            with open(make_file_path, "w") as make_file:
                make_file.write(output)
                print(f"Done! Generated {make_file_path}")