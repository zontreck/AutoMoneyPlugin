pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Build') {
            agent {
                label 'linux'
            }
            steps {
                echo 'Building..'
                script {
                    sh '''
                    #!/bin/bash

                    chmod +x gradlew
                    ./gradlew clean build release
                    '''
                }
            }

            post {
                always {
                    archiveArtifacts artifacts: 'build/libs/*.jar'
                }
            }
        }
    }
}