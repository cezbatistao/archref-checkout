#!/bin/bash
echo 'Installing dependencies ...'
{
    echo 'Build application ...'
    mvn clean package -DskipTests -q -f ../app/pom.xml

    echo 'Setting up...'
    mvn spring-boot:run -Dsqlite4java.library.path=target/dependencies &

    echo 'Starting application...'
    mvn exec:exec

    echo 'Application Started up, initiating tests'
    mvn clean compile -DskipTests -f pom.xml

    echo 'Run component test'
    mvn verify -f pom.xml -Dspring.profiles.active=test

} || {
  echo "Error when try to run component test"
  exit 1
}

echo 'Test finished'
