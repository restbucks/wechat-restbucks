#!/bin/bash -xe

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. "$script_dir"/common.sh #use quote here to compliant with space in dir

version=$(cat "$project_home"/build/version)

docker build -t "$test_image:$version" -f "$project_home"/src/test/docker/Dockerfile "$project_home"