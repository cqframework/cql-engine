#!/usr/bin/env bash

# Fail if any command fails or if there are unbound variables and check syntax
set -euxo pipefail
bash -n "$0"

CMD="mvn verify -T 4 -B -V"

if [[ ! -z "$TRAVIS_TAG" ]]
then
    echo "Building release profile for Travis tag: $TRAVIS_TAG"
    echo $GPG_SECRET_KEYS | base64 --decode| $GPG_EXECUTABLE --import;
    echo $GPG_OWNERTRUST | base64 --decode | $GPG_EXECUTABLE --import-ownertrust;
    CMD="$CMD -P release"
fi

eval $CMD
