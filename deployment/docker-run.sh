#!/bin/bash

echo '<<<---  Rodando image docker $DOCKER_USERNAME/petz-api --->>>'

docker pull tperrut/petz-api
echo '<<<---  Start container  --->>>'

docker ps -a

docker run -p 8080:8080 $DOCKER_USERNAME/petz-api 






