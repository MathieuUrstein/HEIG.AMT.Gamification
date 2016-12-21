#!/usr/bin/env sh

set -e

mkdir -p images/maven/gamification
cp -r src/ images/maven/gamification/
cp pom.xml images/maven/gamification/

docker-compose up --build
