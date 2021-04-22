#!/usr/bin/env bash

# Fail if any command fails or if there are unbound variables and check syntax
set -euxo pipefail
bash -n "$0"

mvn versions:update-properties versions:use-releases versions:use-latest-releases -Dexcludes=org.jvnet.jaxb2_commons:jaxb2-basics:jar:0.12.0,org.eclipse.persistence:org.eclipse.persistence.moxy:jar:2.7.7,org.slf4j:\*
