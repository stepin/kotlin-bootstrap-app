#!/usr/bin/env bash
cd "$(dirname "$0")"
set -xeEuo pipefail

cd services

# Postgres

mkdir -p /data/postgres
podman create --name postgres \
  --network=podman \
  -e "POSTGRES_USER=kotlin-bootstrap-app" \
  -e 'POSTGRES_PASSWORD=SomeP2assword!@e' \
  -e "POSTGRES_DB=kotlin-bootstrap-app_dev" \
  -e "PGDATA=/var/lib/postgresql/data/pgdata" \
  -p 127.0.0.1:5432:5432 \
  -v /data/postgres:/var/lib/postgresql/data \
  postgres:15.2
podman generate systemd --new --files --name postgres


# kotlin-bootstrap-app

mkdir -p /data/kotlin-bootstrap-app-images
podman create --name kotlin-bootstrap-app \
  --network=podman \
  -p 127.0.0.1:80:8080 \
  -e QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://postgres:5432/kotlin-bootstrap-app_dev \
  -e QUARKUS_DATASOURCE_REACTIVE_URL=postgresql://postgres:5432/kotlin-bootstrap-app_dev \
  -e 'QUARKUS_DATASOURCE_PASSWORD=SomeP2assword!@e' \
  -e IMAGES_PATH=/data/kotlin-bootstrap-app-images \
  -v /data/kotlin-bootstrap-app-images/:/data \
  kotlin-bootstrap-app
podman generate systemd --new --files --name kotlin-bootstrap-app
