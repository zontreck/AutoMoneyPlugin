pipeline {
    agent any


    stages {
        stage("Build") {
            agent {
                label "linux"
            }

            environment {
                JAVA_HOME = "${tool 'jdk21'}"
                PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
            }

            steps {
                script {
                    sh '''
                    #!/bin/bash
                    chmod +x gradlew
                    ./gradlew build jar
                    '''
                }
            }

            post {
                always {
                    archiveArtifacts artifacts: "build/libs/*.jar"
                    archiveArtifacts artifacts: "**/build/libs/*.jar"
                    cleanWs()
                }
            }
        }
    }
}