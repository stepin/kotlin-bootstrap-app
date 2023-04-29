#!/usr/bin/env bash
cd "$(dirname "$0")"
set -xeEuo pipefail

cd ..

./gradlew dokkaHtml
echo
echo Docs:
echo $(pwd)/build/dokka/html/index.html
