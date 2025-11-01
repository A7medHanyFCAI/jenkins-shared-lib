@Library('jenkins-shared-lib') _

pipeline {
    agent any

    environment {
        MVN_HOME = tool name: 'Maven3', type: 'maven'
        JAVA_HOME = tool name: 'Java21', type: 'hudson.model.JDK'
        PATH = "${env.JAVA_HOME}/bin:${env.MVN_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Clone Application Code') {
            steps {
                echo "Cloning the Scripted-Pipeline app..."
                git branch: 'main', url: 'https://github.com/A7medHanyFCAI/Scripted-Pipeline.git'
                sh 'ls -la'
            }
        }

        stage('Build') {
            steps {
                script {
                    pipelineUtils.buildApp()
                }
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    pipelineUtils.dockerBuildAndPush()
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    pipelineUtils.deployApp()
                }
            }
        }
    }

    post {
        success {
            echo "SharedLib pipeline completed successfully!"
        }
        failure {
            echo "SharedLib pipeline failed!"
        }
    }
}
