#!/bin/bash
echo 'Installing dependencies ...'
{
    echo 'Build application ...' &&
    mvn clean package -DskipTests -q -f ../app/pom.xml &&

    echo 'Build and starting docker for Application ...' &&
    docker-compose build &&
    docker-compose up -d &&

    echo 'Docker started up, waiting spring-boot to kick start ...' &&
    count=1 &&
    until $(curl -sSf http://172.17.0.1:8383/actuator/health > /dev/null); do
        printf 'Waiting ...\n'
        sleep 5
        if [ $count -gt 20 ]
        then
            echo 'Application is not up, abortion operation....'
            exit 1
        fi
        let "count+=1"
    done &&

    echo 'Application Started up, initiating tests'
    mvn clean compile -DskipTests -f pom.xml &&

    echo 'Run performance test' &&
    mvn gatling:execute -Dgatling.useOldJenkinsJUnitSupport=true -DskipTests -f pom.xml &&

    echo 'Run component test' &&
    mvn verify -f pom.xml

} || {
  echo "Error when try to run component test"
  exit 1
}

echo 'deleting docker containers and images'
docker-compose down

echo 'Test finished'
