#!/bin/bash -xe

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. "$script_dir"/common.sh #use quote here to compliant with space in dir


"$script_dir"/docker-registry-login

version=$(cat "$project_home"/build/version)

docker tag "$test_image:$version" "localhost:5000/$test_image:$version"
docker push "localhost:5000/$test_image:$version"
