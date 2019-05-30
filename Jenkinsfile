node {

    def mvn_version = 'mvn-3_5_4'

    stage('clone-sources') {
        git([url: 'https://carlosz-cit@bitbucket.org/carlosz-cit/archref.git',
             branch: 'master',
             credentialsId: 'bitbucket'])
    }

    stage('unit-tests') {
        echo 'running unit tests, integration tests and static code analysis...'
        try {
            dir ('app') {
                withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
                  sh "mvn clean verify sonar:sonar -PdockerRun -Ptest"
                }
            }
        } finally {
            junit 'app/target/surefire-reports/*.xml'
            publishHTML (target: [ allowMissing: false,
                                   alwaysLinkToLastBuild: false,
                                   keepAll: true,
                                   reportDir: 'app/target/spock/',
                                   reportFiles: 'index.html',
                                   reportName: "Spock Report"
            ])
        }
    }

    stage('component-tests') {
        try {
            echo 'running component tests and performance tests...'
            dir ('component-test') {
                withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
                    sh "./run_test.sh"
                    //sh "mkdir -p target/gatling_results/"
                    //sh "cp -r target/gatling/*/. target/gatling_results/"
                }
            }
        } finally {
            junit 'component-test/target/surefire-reports/*.xml'
            publishHTML (target: [ allowMissing: false,
                                   alwaysLinkToLastBuild: false,
                                   keepAll: true,
                                   reportDir: 'component-test/target/cucumber',
                                   reportFiles: 'index.html',
                                   reportName: "Cucumber Features Report"
            ])
            /*
            publishHTML (target: [ allowMissing: false,
                                   alwaysLinkToLastBuild: false,
                                   keepAll: true,
                                   reportDir: 'component-test/target/gatling_results',
                                   reportFiles: 'index.html',
                                   reportName: "Gatling Report"
            ])
            */
            dir ('component-test') {
                gatlingArchive()
            }
        }
    }

    stage('publish-version') {
        echo 'publish version to artifactory...'
        dir ('app') {
            withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
                withCredentials([usernamePassword(
                        credentialsId: 'docker-credentials',
                        usernameVariable: 'USERNAME',
                        passwordVariable: 'PASSWORD')]) {
                    sh "mvn clean package -DskipTests -Dbuild.number=${env.BUILD_NUMBER} -Ddocker.username=${USERNAME} -Ddocker.password=${PASSWORD} docker:build docker:push"
                }
            }
        }
    }

    stage('release-version') {
        echo 'release version and generate new one...'
        withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
            withCredentials([usernamePassword(
                    credentialsId: 'bitbucket',
                    usernameVariable: 'GIT_USERNAME',
                    passwordVariable: 'GIT_PASSWORD')]) {
                sh "mvn initialize -batch-mode release:clean release:prepare"
            }
        }
    }
}