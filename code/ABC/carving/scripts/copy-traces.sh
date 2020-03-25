#!/bin/bash

# Currently only the -p argument is available
arg=$1

# Name of the application package to copy traces from
package_name=$2

output_dir=$(realpath "$(dirname "$(realpath "$0")")/../traces/$package_name")

if [ "$arg" != "-p" ]; then
  echo "Please specify the package name argument -p" && exit
fi

if [ -z "$package_name" ]; then
  echo "Package name argument -p must not be empty" && exit
fi

# Create dirs if they do not exist already
mkdir -p tmp
mkdir -p "$output_dir"

# Restart daemon with root access in order to be able to access app data
adb root

# Apparently, adb cannot copy files using wildcards, hence, we copy the whole package temporarily
adb pull "/data/data/$package_name" ./tmp

# Iterate over trace files and copy them to the output dir
for filename in ./tmp/"$package_name"/Trace-*.txt; do
  file=$(basename "$filename")
  cp "$filename" "$output_dir"/"$file"
done

# Remove temporary files
rm -r ./tmp

echo "Done. Traces were copied to" "$output_dir"
