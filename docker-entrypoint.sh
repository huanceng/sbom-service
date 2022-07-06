#!/usr/bin/env bash

set -e

WORKSPACE=/opt/sbom-service

cd ${WORKSPACE}
git pull
git submodule update --recursive

/bin/bash start-sbom-service.sh
