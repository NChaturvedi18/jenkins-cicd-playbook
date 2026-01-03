# JavaScript/Node.js Pipeline Examples

This directory contains Jenkins CI/CD pipeline examples specifically adapted for JavaScript and Node.js projects. These examples demonstrate how to build, test, and deploy JavaScript applications using npm/yarn and Node.js.

## Directory Structure

- **[freestyle-jobs/](freestyle-jobs/)**: Freestyle job pipeline for JavaScript projects
  - `Jenkinsfile`: Declarative pipeline for freestyle Jenkins jobs using Node.js
- **[multibranch/](multibranch/)**: Multibranch pipeline for JavaScript projects
  - `Jenkinsfile`: Declarative pipeline for multibranch jobs using Node.js
- **[shared-library/](shared-library/)**: Reusable shared library functions for JavaScript projects
  - `vars/buildAndTest.groovy`: Shared library function for building and testing JavaScript projects

## Prerequisites

- Jenkins server with the following plugins:
  - Pipeline
  - NodeJS Plugin
  - Git
  - JUnit (for test reporting)
- Node.js installation configured in Jenkins (Global Tool Configuration)
- A JavaScript project with `package.json`

## Key Features

### Build Process
- **Dependency Installation**: Uses `npm install` to install project dependencies
- **Build Execution**: Runs `npm run build` (configurable via package.json scripts)
- **Artifact Archiving**: Archives `dist/`, `build/`, or other build output directories

### Testing
- **Unit Tests**: Executes `npm test` for running test suites
- **Integration Tests**: Supports `npm run integration-test` for integration testing
- **Test Reporting**: Publishes JUnit XML results (requires test framework configuration like jest-junit)

### Deployment
- **Branch-based Deployment**: Different deployment stages based on Git branches
- **NPM Publishing**: Support for publishing to npm registries
- **Environment-specific**: Dev, staging, and production deployments

## Configuration

### package.json Scripts
Ensure your `package.json` includes these scripts:
```json
{
  "scripts": {
    "build": "your-build-command",
    "test": "your-test-command",
    "integration-test": "your-integration-test-command",
    "sanity": "your-sanity-test-command"
  }
}
```

### Test Framework Setup
For JUnit test reporting, configure your test framework to output XML results:
- **Jest**: Use `jest-junit` package and configure output to `test-results/`
- **Mocha**: Use `mocha-junit-reporter`
- **Other frameworks**: Ensure XML output goes to `test-results/**/*.xml`

### Jenkins Node.js Configuration
1. Go to Jenkins → Manage Jenkins → Global Tool Configuration
2. Add NodeJS installation with your preferred version
3. Reference it in pipelines as `nodeJSInstallationName: 'node'`

## Usage Examples

### Freestyle Job Setup
1. Create a new Freestyle project in Jenkins
2. Configure Git repository
3. Add parameters for branch selection and test options
4. Use the `Jenkinsfile` from this directory

### Multibranch Pipeline Setup
1. Create a new Multibranch Pipeline project
2. Add your Git repository as branch source
3. Jenkins will automatically discover branches and create jobs
4. Use the `Jenkinsfile` from this directory

### Shared Library Usage
Call the shared library function in your pipeline:
```groovy
@Library('your-shared-library') _

pipeline {
    agent any
    stages {
        stage('Build & Test') {
            steps {
                buildAndTestJS(
                    skipTests: false,
                    runIntegrationTests: true,
                    nodeVersion: 'node',
                    buildScript: 'build',
                    testScript: 'test'
                )
            }
        }
    }
}
```

## Customization

### Build Output Directories
Modify the archive patterns in the pipeline to match your build output:
```groovy
archiveArtifacts artifacts: 'your-output-dir/**/*', fingerprint: true
```

### Additional Test Types
Add custom test stages as needed:
```groovy
stage('E2E Tests') {
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run e2e-test'
        }
    }
}
```

### Deployment Configuration
Customize deployment steps for your infrastructure:
```groovy
stage('Deploy to AWS') {
    when { branch 'main' }
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run deploy'
        }
    }
}
```

## Troubleshooting

### Common Issues
- **Node.js not found**: Ensure NodeJS plugin is installed and configured
- **npm install fails**: Check network connectivity and registry configuration
- **Test results not found**: Verify test framework outputs XML to expected location
- **Build fails**: Check package.json scripts and dependencies

### Debug Tips
- Add `sh 'node --version && npm --version'` to verify installations
- Use `echo` statements to debug pipeline variables
- Check Jenkins build logs for detailed error messages

## Contributing

When contributing JavaScript pipeline examples:
1. Test with multiple Node.js versions
2. Include sample package.json configurations
3. Document any required Jenkins plugins
4. Provide troubleshooting guidance

## Related Documentation

- [Main README](../README.md) - Overview of all pipeline examples
- [Java Examples](../JAVA/) - Java/Maven pipeline examples
- [Jenkins NodeJS Plugin](https://plugins.jenkins.io/nodejs/) - Plugin documentation