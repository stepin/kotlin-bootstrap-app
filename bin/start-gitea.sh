#!/usr/bin/env bash
#
# This is more example than for real usage -- most probably you want it as always running service.
#
cd "$(dirname "$0")"
set -xeEuo pipefail

docker run -d --name "gitea" \
  --stop-timeout 3600 \
  -e GITEA__actions__ENABLED=true \
  -p 3000:3000 \
  gitea/gitea
echo
echo http://localhost:3000
echo 1. Включить локальный режим и Далее.
echo 2. Register
echo 3. Create private repo
echo 4. git remote add origin http://localhost:3000/admin1/kotlin-bootstrap-app.git
echo 5. git push -u origin main


#docker exec -it gitea bash -c "echo '[actions]' >> /data/gitea/conf/app.ini"
#docker exec -it gitea bash -c "echo 'ENABLED = true' >> /data/gitea/conf/app.ini"
#docker exec -it gitea cat /data/gitea/conf/app.ini
#docker stop gitea
#docker start gitea

echo http://localhost:3000/admin/runners to create token for runner
echo enable actions on repo level: http://localhost:3000/admin1/kotlin-bootstrap-app/settings
echo http://localhost:3000/admin1/kotlin-bootstrap-app/actions -- this should be visible

docker run -d --name gitea_runner_1 \
  -e GITEA_INSTANCE_URL=http://172.17.0.1:3000 \
  -e GITEA_RUNNER_REGISTRATION_TOKEN=sv6HrMP9tx95NWbU5eMhuUIFQAe14BRYLCDQrz5j \
  -v /var/run/docker.sock:/var/run/docker.sock \
   gitea/act_runner
