// vars/buildAndTestJS.groovy
// Example shared library function for building, testing, deploying, and archiving a JavaScript project

def call(Map config = [:]) {
    // Default values
    def skipTests = config.skipTests ?: false
    def runIntegrationTests = config.runIntegrationTests ?: false
    def nodeVersion = config.nodeVersion ?: 'node'
    def npmCommand = config.npmCommand ?: 'npm'
    def buildScript = config.buildScript ?: 'build'
    def testScript = config.testScript ?: 'test'
    def withSonar = config.withSonar ?: false
    def deployTo = config.deployTo ?: null

    stage('Build') {
        nodejs(nodeJSInstallationName: nodeVersion) {
            sh "${npmCommand} install"
            sh "${npmCommand} run ${buildScript}"
        }
    }

    stage('Test') {
        if (!skipTests) {
            stage('Unit Tests') {
                nodejs(nodeJSInstallationName: nodeVersion) {
                    sh "${npmCommand} run ${testScript}"
                    junit 'test-results/**/*.xml'  // Adjust path based on test framework (e.g., Jest with jest-junit)
                }
            }

            if (runIntegrationTests) {
                stage('Integration Tests') {
                    nodejs(nodeJSInstallationName: nodeVersion) {
                        sh "${npmCommand} run integration-test"  // Assuming you have this script in package.json
                        junit 'test-results/integration/**/*.xml'
                    }
                }
            }
        }

        if (withSonar) {
            stage('SonarQube Analysis') {
                withSonarQubeEnv('SonarQube') {
                    nodejs(nodeJSInstallationName: nodeVersion) {
                        sh "${npmCommand} run sonar"  // Assuming sonar script in package.json using sonar-scanner
                    }
                }
            }
        }
    }

    stage('Deploy') {
        if (deployTo) {
            // For JS, deployment might be to npm registry or cloud platform
            nodejs(nodeJSInstallationName: nodeVersion) {
                sh "${npmCommand} publish --registry ${deployTo}"
            }
        }
    }

    stage('Archive') {
        archiveArtifacts artifacts: 'dist/**/*,build/**/*', fingerprint: true  // Adjust based on build output directory
    }
}