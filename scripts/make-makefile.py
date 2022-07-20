import configparser
import os
import re
import sys

from jinja2 import Template

script_path = os.path.realpath(__file__)
scripts_dir = os.path.dirname(script_path)
template_path = os.path.join(scripts_dir, "make-template.make")


def parse_zip_align(config_values):
    if "make_zipalign" in config_values and "true" in config_values["make_zipalign"]:
        config_values["make_zipalign"] = True
    else:
        config_values["make_zipalign"] = False


def parse_java_opts(config_values):
    if "make_trace_multiple_threads" in config_values and "true" in config_values["make_trace_multiple_threads"]:
        config_values["make_trace_multiple_threads"] = " -Dabc.instrument.multithreaded "
    else:
        config_values["make_trace_multiple_threads"] = ""


def parse_permissions(config_values):
    if "make_permissions" in config_values and config_values["make_permissions"]:
        permissions = config_values["make_permissions"]
        config_values["permissions"] = [f"$(ADB) shell pm grant {config_values['make_app_debug_package']} {p};\\" for p
                                        in permissions.split(",")]
    else:
        config_values["permissions"] = []


def parse_carving_options(config_values):
    if "make_carving_filter_methods" in config_values and config_values["make_carving_filter_methods"]:
        filter_methods = config_values["make_carving_filter_methods"]
        config_values["make_carving_filter_methods"] = [f"--filter-method {method} \\" for method in
                                        re.split("\s*,\s*", filter_methods)]
    else:
        config_values["make_carving_filter_methods"] = []


def parse_instrumentation_options(config_values):
    if "make_instr_skip_classes" in config_values and config_values["make_instr_skip_classes"]:
        skip_classes = config_values["make_instr_skip_classes"]
        config_values["make_instr_skip_classes"] = [f"--skip-class={clazz} \\" for clazz in
                                                    re.split("\s*,\s*", skip_classes)]
    else:
        config_values["make_instr_skip_classes"] = []

    if "make_instr_filter_packages" in config_values and config_values["make_instr_filter_packages"]:
        filter_packages = config_values["make_instr_filter_packages"]
        config_values["make_instr_filter_packages"] = [f"--filter-package={package} \\" for package in
                                                       re.split("\s*,\s*", filter_packages)]
    else:
        config_values["make_instr_filter_packages"] = []

    if "make_instr_filter_classes" in config_values and config_values["make_instr_filter_classes"]:
        filter_classes = config_values["make_instr_filter_classes"]
        config_values["make_instr_filter_classes"] = [f"--filter-class={clazz} \\" for clazz in
                                                      re.split("\s*,\s*", filter_classes)]
    else:
        config_values["make_instr_filter_classes"] = []


if __name__ == '__main__':
    if len(sys.argv) == 1:
        print("Missing application root directory")
    else:
        config_path = os.path.join(sys.argv[1], "abc-apk-config")
        if not os.path.isdir(sys.argv[1]) or not os.path.isfile(config_path):
            print(f"{config_path} does not exist")
        else:
            config = configparser.ConfigParser()
            config.read(config_path)
            config_values = dict(config["MakeConfiguration"].items())

            parse_java_opts(config_values)
            parse_permissions(config_values)
            parse_carving_options(config_values)
            parse_instrumentation_options(config_values)
            parse_zip_align(config_values)

            with open(template_path, "r") as template_file:
                t = Template(template_file.read())
                output = t.render(config_values)
                make_file_path = os.path.join(sys.argv[1], "makefile")
                with open(make_file_path, "w") as make_file:
                    make_file.write(output)
                    print(f"Done! Generated {make_file_path}")
