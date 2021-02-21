#!/bin/bash
echo '<<<--- Realizando build da img docker --->>>'

echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
#docker build -t $DOCKER_USERNAME/petz-api

mvn jib:dockerBuild 

docker run -p 8080:8080 $DOCKER_USERNAME/petz-api 





