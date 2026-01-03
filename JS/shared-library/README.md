# JavaScript Shared Library

This directory contains reusable Jenkins shared library code specifically designed for JavaScript and Node.js projects. Shared libraries enable code reuse across multiple pipelines and provide standardized build, test, and deployment functions.

## Directory Structure

- **`vars/`**: Contains the shared library functions
  - **`buildAndTest.groovy`**: Main shared library function for JavaScript projects

## Overview

The shared library provides:
- **Standardized Build Process**: Consistent build steps across projects
- **Flexible Configuration**: Customizable parameters for different project needs
- **Code Reuse**: Eliminate pipeline code duplication
- **Maintainability**: Central location for pipeline logic updates

## Shared Library Function: buildAndTest

### Purpose
The `buildAndTest` function provides a complete CI/CD pipeline for JavaScript projects, handling build, test, deployment, and artifact archiving in a reusable package.

### Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `skipTests` | boolean | `false` | Skip all test execution |
| `runIntegrationTests` | boolean | `false` | Enable integration test execution |
| `nodeVersion` | string | `'node'` | Node.js installation name in Jenkins |
| `npmCommand` | string | `'npm'` | Package manager command (npm or yarn) |
| `buildScript` | string | `'build'` | npm script name for building |
| `testScript` | string | `'test'` | npm script name for testing |
| `withSonar` | boolean | `false` | Enable SonarQube analysis |
| `deployTo` | string | `null` | npm registry URL for deployment |

### Pipeline Stages

#### Build Stage
- Sets up Node.js environment using the specified version
- Installs dependencies with `npm install`
- Executes build script (default: `npm run build`)

#### Test Stage
- **Unit Tests**: Runs test script and publishes JUnit results
- **Integration Tests**: Optional integration testing (when enabled)
- **SonarQube Analysis**: Optional code quality analysis (when enabled)

#### Deploy Stage
- Publishes to npm registry (when `deployTo` is specified)
- Supports private registries with authentication

#### Archive Stage
- Archives build artifacts (`dist/`, `build/` directories)
- Enables artifact fingerprinting for tracking

## Usage Examples

### Basic Usage
```groovy
@Library('your-shared-library') _

pipeline {
    agent any
    stages {
        stage('CI/CD') {
            steps {
                buildAndTest()
            }
        }
    }
}
```

### Advanced Configuration
```groovy
@Library('your-shared-library') _

pipeline {
    agent any
    stages {
        stage('Build & Test') {
            steps {
                buildAndTest(
                    skipTests: false,
                    runIntegrationTests: true,
                    nodeVersion: 'node-16',
                    npmCommand: 'yarn',
                    buildScript: 'build:prod',
                    testScript: 'test:ci',
                    withSonar: true,
                    deployTo: 'https://registry.npmjs.org/'
                )
            }
        }
    }
}
```

### Integration with Other Pipelines
```groovy
@Library('your-shared-library') _

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-org/your-repo.git'
            }
        }
        stage('Build & Test') {
            steps {
                buildAndTest(
                    runIntegrationTests: true,
                    withSonar: true
                )
            }
        }
        stage('Deploy') {
            when { branch 'main' }
            steps {
                // Custom deployment logic
                sh 'deploy-to-production.sh'
            }
        }
    }
}
```

## Prerequisites

### Jenkins Setup
- Shared Library plugin configured
- NodeJS plugin installed
- Node.js tools configured in Global Tool Configuration
- SonarQube plugin (if using code analysis)

### Project Requirements
Your JavaScript project must have a `package.json` with appropriate scripts:
```json
{
  "scripts": {
    "build": "webpack --mode production",
    "test": "jest --coverage --watchAll=false",
    "integration-test": "cypress run",
    "sonar": "sonar-scanner"
  }
}
```

### Test Framework Configuration
For JUnit reporting, configure your test framework:
- **Jest**: Use `jest-junit` reporter
- **Mocha**: Configure `mocha-junit-reporter`
- **Output**: Ensure XML files go to `test-results/**/*.xml`

## Shared Library Setup

### Global Shared Library
1. Go to Jenkins → Manage Jenkins → Configure System
2. Scroll to "Global Pipeline Libraries"
3. Add library configuration:
   - Name: `your-shared-library`
   - Source: Git repository containing this shared library
   - Branch: `main` (or appropriate branch)

### Folder-level Shared Library
1. Go to your Jenkins folder → Configure
2. Add "Pipeline Libraries" configuration
3. Configure library source and settings

### Pipeline-level Usage
```groovy
library identifier: 'your-shared-library@tag-or-branch',
        retriever: modernSCM([
            $class: 'GitSCMSource',
            remote: 'https://github.com/your-org/shared-library.git'
        ])
```

## Customization

### Extending the Function
Create a wrapper function for project-specific needs:
```groovy
// In your pipeline or another shared library
def buildAndTestReact(Map config = [:]) {
    def defaultConfig = [
        buildScript: 'build:react',
        testScript: 'test:react'
    ] + config

    buildAndTest(defaultConfig)
}
```

### Adding Custom Stages
Extend the pipeline with additional stages:
```groovy
@Library('your-shared-library') _

pipeline {
    agent any
    stages {
        stage('Build & Test') {
            steps {
                buildAndTest(skipTests: false)
            }
        }
        stage('Custom Deployment') {
            steps {
                // Your custom deployment logic
                sh 'deploy-to-custom-environment.sh'
            }
        }
    }
}
```

### Environment-Specific Configuration
```groovy
def getBuildConfig() {
    if (env.BRANCH_NAME == 'main') {
        return [
            buildScript: 'build:production',
            deployTo: 'https://registry.npmjs.org/'
        ]
    } else {
        return [
            buildScript: 'build:development'
        ]
    }
}

pipeline {
    stages {
        stage('Build & Test') {
            steps {
                script {
                    def config = getBuildConfig()
                    buildAndTest(config)
                }
            }
        }
    }
}
```

## Troubleshooting

### Common Issues
- **Library not found**: Verify shared library configuration and name
- **Node.js version mismatch**: Check NodeJS tool configuration
- **Script not found**: Ensure package.json has required scripts
- **Test results missing**: Verify test framework XML output
- **Deployment fails**: Check npm registry authentication

### Debug Steps
1. Add debug output: `echo "Using config: ${config}"`
2. Test individual stages manually
3. Check Jenkins shared library logs
4. Verify parameter passing

### Performance Optimization
- Use appropriate Node.js versions for your projects
- Cache node_modules when possible
- Configure parallel test execution
- Use lightweight base images for agents

## Best Practices

### Library Organization
- Keep functions focused and reusable
- Use meaningful parameter names
- Provide sensible defaults
- Document all parameters and usage

### Version Management
- Use specific versions/tags for stability
- Test library changes across projects
- Maintain backward compatibility
- Document breaking changes

### Security Considerations
- Avoid hardcoding credentials
- Use Jenkins credentials for sensitive data
- Validate input parameters
- Follow principle of least privilege

## Related Documentation

- [JS README](../README.md) - JavaScript examples overview
- [Main Shared Library](../../shared-library/) - General shared library examples
- [Jenkins Shared Libraries](https://jenkins.io/doc/book/pipeline/shared-libraries/) - Official documentation