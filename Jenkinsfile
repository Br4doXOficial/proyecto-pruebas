pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipUnitTests=true -DskipIntegrationTests=true -DskipAcceptanceTests=true'
            }
        }
        stage('Unit tests') {
            steps { sh 'mvn -B test' }
        }
        stage('Integration tests') {
            steps { sh 'mvn -B verify -DskipUnitTests=true' }
        }
        stage('Acceptance tests') {
            steps {
                sh 'mvn -B verify -DskipUnitTests=true -DskipIntegrationTests=true -DskipAcceptanceTests=false'
            }
        }
        stage('Deploy Blue-Green') {
            steps {
                sh './scripts/deploy-blue-green.sh target/inventario-automatizado-1.0.0.jar'
                sh './scripts/smoke-test.sh'
            }
        }
    }

    post {
        failure {
            sh './scripts/rollback.sh || true'
        }
        always {
            junit allowEmptyResults: true, testResults: 'target/*-reports/*.xml'
            archiveArtifacts artifacts: 'target/*.jar,deploy/**/*', allowEmptyArchive: true
        }
    }
}
