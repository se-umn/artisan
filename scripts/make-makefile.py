import os
import sys
from string import Template
import configparser

script_path = os.path.realpath(__file__)
scripts_dir = os.path.dirname(script_path)
template_path = os.path.join(scripts_dir, "make-template.make")

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
        with open(template_path, "r") as template_file:
            template = Template(template_file.read())
            output = template.substitute(config_values)
            make_file_path = os.path.join(sys.argv[1], "makefile")
            with open(make_file_path, "w") as make_file:
                make_file.write(output)
                print(f"Done! Generated {make_file_path}")