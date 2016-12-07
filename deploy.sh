#!/usr/bin/env sh

set -e

# build
mvn package

# deploy
cp target/gamification-0.0.1-SNAPSHOT.jar images/open-jdk/gamification.jar
docker-compose up --build
