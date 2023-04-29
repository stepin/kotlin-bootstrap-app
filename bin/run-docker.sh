#!/usr/bin/env bash
cd "$(dirname "$0")"
set -xeEuo pipefail

docker run --rm -i \
  -p 8080:8080 \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://172.17.0.1:55000/kotlin-bootstrap-app_dev \
  -e QUARKUS_DATASOURCE_REACTIVE_URL=postgresql://172.17.0.1:55000/kotlin-bootstrap-app_dev \
  kotlin-bootstrap-app
