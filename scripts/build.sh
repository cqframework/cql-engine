#!/usr/bin/env bash

# Fail if any command fails or if there are unbound variables and check syntax
set -euxo pipefail
bash -n "$0"

# Run tests
CMD="mvn test -T 4 -B"
if [[ "$TRAVIS_BRANCH" =~ master* ]]; then CMD="$CMD -P release"; fi
eval $CMD

# If it's not develop or master, exit
if ! [[ "$TRAVIS_BRANCH" =~ master || "$TRAVIS_BRANCH" =~ develop ]]; then exit 0; fi

# Run Deploy / Package
CMD="mvn deploy -T 4 -B -DskipTests=true"

# Import maven settings
cp .travis.settings.xml $HOME/.m2/settings.xml

# Import signing key
if [[ "$TRAVIS_BRANCH" =~ master* ]]; then
    echo $GPG_SECRET_KEYS | base64 --decode| $GPG_EXECUTABLE --import;
    echo $GPG_OWNERTRUST | base64 --decode | $GPG_EXECUTABLE --import-ownertrust;
    CMD="$CMD -P release"
fi

eval $CMD