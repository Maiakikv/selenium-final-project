pipeline {
  agent any
  stages {
    stage('build') {
      parallel {
        stage('build') {
          steps {
            bat 'mvn clean test'
          }
        }

        stage('maven version') {
          steps {
            bat 'mvn --version'
          }
        }

      }
    }

  }
}