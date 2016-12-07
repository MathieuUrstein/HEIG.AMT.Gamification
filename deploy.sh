#!/usr/bin/env sh

set -e

# build
mvn package

# deploy
cp target/gamification.jar images/open-jdk/
docker-compose up --build
