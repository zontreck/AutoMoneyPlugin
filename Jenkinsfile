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

                    mvn clean package
                    '''
                }
            }

            post {
                always {
                    archiveArtifacts artifacts: 'target/*.jar'
                }
            }
        }
    }
}