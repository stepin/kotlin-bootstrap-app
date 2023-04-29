#!/usr/bin/env bash
set -eEuo pipefail
cd "$(dirname "$0")"
set -x

cd ..

./gradlew build -Dquarkus.package.type=fast-jar
docker build -f src/main/docker/Dockerfile.jvm -t kotlin-bootstrap-app .
