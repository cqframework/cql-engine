#!/usr/bin/env bash

# Fail if any command fails or if there are unbound variables and check syntax
set -euxo pipefail
bash -n "$0"

# jaxb2 commons version 1.11.1 is bugged
mvn versions:update-properties versions:use-releases versions:use-latest-releases -Dexcludes=org.jvnet.jaxb2_commons:*:jar:1.11.\*,org.eclipse.persistence:org.eclipse.persistence.moxy:jar:2.7.7,org.slf4j:\*

