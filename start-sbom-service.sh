#!/usr/bin/env bash

set -e

export DB_PASSWORD=$(cat ${DB_PASSWORD_FILE})

WORKSPACE=/opt/sbom-service

cd ${WORKSPACE}

/bin/bash gradlew bootWar

java -jar ${WORKSPACE}/build/libs/sbom-service-1.0-SNAPSHOT.war