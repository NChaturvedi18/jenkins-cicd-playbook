// vars/buildAndTest.groovy
// Example shared library function for building and testing a Maven project

def call(Map config = [:]) {
    // Default values
    def skipTests = config.skipTests ?: false
    def runIntegrationTests = config.runIntegrationTests ?: false

    stage('Build') {
        sh "mvn -B -DskipTests=${skipTests} clean package"
    }

    if (!skipTests) {
        stage('Unit Tests') {
            sh 'mvn -B test'
            junit 'target/surefire-reports/**/*.xml'
        }

        if (runIntegrationTests) {
            stage('Integration Tests') {
                sh 'mvn -B -Pintegration verify'
                junit 'target/failsafe-reports/**/*.xml'
            }
        }
    }

    stage('Archive') {
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
}