// vars/buildAndTest.groovy
// Example shared library function for building and testing a Maven project

def call(Map config = [:]) {
    // Default values
    def skipTests = config.skipTests ?: false
    def runIntegrationTests = config.runIntegrationTests ?: false
    def mavenGoals = config.mavenGoals ?: 'clean package'
    def withSonar = config.withSonar ?: false
    def jdkVersion = config.jdkVersion ?: 'default'
    def mavenVersion = config.mavenVersion ?: 'maven-3.8.6'

    stage('Build') {
        withMaven(maven: mavenVersion, jdk: jdkVersion) {
            sh "mvn -B -DskipTests=${skipTests} ${mavenGoals}"
        }
    }

    if (!skipTests) {
        stage('Unit Tests') {
            withMaven(maven: mavenVersion, jdk: jdkVersion) {
                sh 'mvn -B test'
                junit 'target/surefire-reports/**/*.xml'
            }
        }

        if (runIntegrationTests) {
            stage('Integration Tests') {
                withMaven(maven: mavenVersion, jdk: jdkVersion) {
                    sh 'mvn -B -Pintegration verify'
                    junit 'target/failsafe-reports/**/*.xml'
                }
            }
        }
    }

    if (withSonar) {
        stage('SonarQube Analysis') {
            withSonarQubeEnv('SonarQube') {
                withMaven(maven: mavenVersion, jdk: jdkVersion) {
                    sh 'mvn -B sonar:sonar'
                }
            }
        }
    }

    stage('Archive') {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
}