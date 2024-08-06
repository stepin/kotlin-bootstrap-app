#!/usr/bin/env bash
cd "$(dirname "$0")"
set -xeEuo pipefail

cd ..

export SONAR_SCANNER_OPTS="-Xmx512m"
./gradlew sonar
