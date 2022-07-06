#!/usr/bin/env bash

set -e

WORKSPACE=/opt/sbom-service

cd ${WORKSPACE}

APP_PROPERTIES_FILE=${WORKSPACE}/src/main/resources/application.properties
sed -i "s/{db_url}/jdbc\:postgresql\:\/\/${DB_HOST}\:${DB_PORT}\/${DB_NAME}/" ${APP_PROPERTIES_FILE}
sed -i "s/{db_username}/${DB_USERNAME}/" ${APP_PROPERTIES_FILE}
sed -i "s/{db_password}/$(cat ${DB_PASSWORD_FILE})/" ${APP_PROPERTIES_FILE}

/bin/bash gradlew bootWar

java -jar ${WORKSPACE}/build/libs/sbom-service-1.0-SNAPSHOT.war