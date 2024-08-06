#!/usr/bin/env bash
#
# It's shell script but it's expected that it will be run line by line by engineer
# that understood what each line do.
#
cd "$(dirname "$0")"
set -xeEuo pipefail

# disable SELinux
setenforce 0
sed -i'' s/SELINUX=enforcing/SELINUX=disabled/ /etc/selinux/config

# install rpms
dnf update
dnf install -y podman mc vim tmux

# add plugin to enable dns connections between pods
dnf install -y go make git dnsmasq
git clone https://github.com/containers/dnsname.git
cd dnsname
make
make install PREFIX=/usr

# create (and later use it) network (it will be with dns support)
# podman network create

# login to docker registry (if required)
# podman login --username XXX docker.io

# create folders for data
mkdir -p /data/kotlin-bootstrap-app-images /data/postgres
chmod -R 777 /data

# start containers
scp services/*.service /etc/systemd/system/
systemctl daemon-reload
systemctl enable --now container-postgres container-kotlin-bootstrap-app

echo please, reboot
