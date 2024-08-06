#!/usr/bin/env bash
cd "$(dirname "$0")"
set -xeEuo pipefail

docker run -d --name "postgres" \
  -e "POSTGRES_USER=kotlin-bootstrap-app" \
  -e "POSTGRES_PASSWORD=SomeP2assword!@e" \
  -e "POSTGRES_DB=kotlin-bootstrap-app_dev" \
  -p 55000:5432 \
  postgres:15.2
