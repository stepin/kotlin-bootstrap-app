#!/usr/bin/env bash
cd "$(dirname "$0")"
set -xeEuo pipefail

# 2Gb is not enough for Docker, at least 4Gb
docker run -d --name "sonarqube" \
  --stop-timeout 3600 \
  -e SONAR_FORCE_AUTHENTICATION=false \
  -e SONAR_AUTOCREATE_USERS=true \
  -p 9000:9000 \
  sonarqube:community

echo http://localhost:9000
echo 1. login admin:admin
echo 2. manually create project
echo 3. create local token
echo 4. update gradle with new token value

