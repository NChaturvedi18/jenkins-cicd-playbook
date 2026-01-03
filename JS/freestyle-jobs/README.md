# JavaScript Freestyle Jobs

This directory contains Jenkins freestyle job examples specifically adapted for JavaScript and Node.js projects. Freestyle jobs provide flexibility for simple CI/CD workflows without the complexity of scripted pipelines.

## Files

- **`Jenkinsfile`**: Declarative pipeline configuration for freestyle Jenkins jobs using JavaScript/Node.js

## Overview

Freestyle jobs in Jenkins are configured through the web UI and are ideal for:
- Simple build and test workflows
- Parameterized builds with user input
- Manual triggering of builds
- Integration with existing Jenkins UI workflows

## Pipeline Features

### Build Stage
- **Node.js Environment**: Uses the NodeJS plugin to set up the Node.js runtime
- **Dependency Installation**: Runs `npm install` to install project dependencies
- **Build Execution**: Executes `npm run build` to compile/transpile the application

### Test Stages
- **Unit Tests**: Runs `npm test` for unit test execution
- **Sanity Tests**: Optional sanity test suite with `npm run sanity`
- **Integration Tests**: Optional integration tests (parameter-controlled)
- **Test Reporting**: Publishes JUnit XML results for test visualization

### Archive Stage
- **Build Artifacts**: Archives `dist/`, `build/`, or other output directories
- **Fingerprinting**: Enables artifact tracking across builds

## Configuration Parameters

The pipeline supports the following parameters:

- **`BRANCH`**: Git branch to build (default: 'master')
- **`RUN_SANITY_TESTS`**: Enable/disable sanity tests (default: true)
- **`RUN_SMOKE_TESTS`**: Enable/disable smoke tests (default: false)
- **`RUN_REGRESSION_TESTS`**: Enable/disable regression tests (default: false)
- **`RUN_INTEGRATION_TESTS`**: Enable/disable integration tests (default: false)

## Prerequisites

### Jenkins Setup
- NodeJS plugin installed
- Node.js tool configured in Global Tool Configuration
- Git plugin for source code management

### Project Setup
Your JavaScript project should have a `package.json` with these scripts:
```json
{
  "scripts": {
    "build": "your-build-command",
    "test": "your-test-command",
    "sanity": "your-sanity-tests",
    "integration-test": "your-integration-tests"
  }
}
```

### Test Configuration
For JUnit test reporting, configure your test framework to output XML:
- **Jest**: Install `jest-junit` and configure output directory
- **Mocha**: Use `mocha-junit-reporter`
- **Other**: Ensure XML files are written to `test-results/**/*.xml`

## Jenkins Job Setup

1. **Create Freestyle Project**
   - Go to Jenkins → New Item → Freestyle project
   - Enter project name and select "Freestyle project"

2. **Source Code Management**
   - Select Git
   - Enter repository URL
   - Configure credentials if needed

3. **Build Parameters**
   - Check "This project is parameterized"
   - Add parameters matching the pipeline configuration

4. **Pipeline Configuration**
   - Add "Pipeline" build step
   - Select "Pipeline script from SCM"
   - Set SCM to Git, repository URL, and script path to `JS/freestyle-jobs/Jenkinsfile`

5. **Post-build Actions**
   - Configure any additional actions (notifications, artifact management)

## Usage

### Manual Execution
- Click "Build Now" to trigger manually
- Select parameters in the build dialog
- Monitor progress in the build console

### Scheduled Builds
- Configure build triggers under "Build Triggers"
- Set up periodic builds or webhook integration

### Build Results
- View test results in the "Test Results" section
- Download artifacts from the "Build Artifacts" section
- Check build logs for detailed execution information

## Customization

### Adding New Test Types
```groovy
stage('E2E Tests') {
    when { expression { return params.RUN_E2E_TESTS } }
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run e2e-test'
        }
    }
    post {
        always { junit 'test-results/e2e/**/*.xml' }
    }
}
```

### Environment-Specific Builds
```groovy
stage('Build for Production') {
    when { expression { return params.ENVIRONMENT == 'production' } }
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run build:prod'
        }
    }
}
```

### Additional Parameters
Add new parameters in the parameters section:
```groovy
booleanParam(name: 'DEPLOY_TO_NPM', defaultValue: false, description: 'Deploy to npm registry')
```

## Troubleshooting

### Common Issues
- **Node.js not found**: Verify NodeJS plugin installation and tool configuration
- **npm install fails**: Check network connectivity and npm registry access
- **Build script not found**: Ensure package.json has the required scripts
- **Test results missing**: Verify test framework XML output configuration

### Debug Steps
1. Add debug output: `sh 'node --version && npm --version'`
2. Check build logs for detailed error messages
3. Verify file paths and permissions
4. Test commands manually in the project directory

## Related Documentation

- [JS README](../README.md) - JavaScript examples overview
- [Main Freestyle Jobs](../../freestyle-jobs/) - General freestyle job examples
- [Jenkins NodeJS Plugin](https://plugins.jenkins.io/nodejs/) - Plugin documentation