# JavaScript Multibranch Pipelines

This directory contains Jenkins multibranch pipeline examples specifically adapted for JavaScript and Node.js projects. Multibranch pipelines automatically create and manage pipelines for each branch in a repository.

## Files

- **`Jenkinsfile`**: Declarative pipeline configuration for multibranch jobs using JavaScript/Node.js

## Overview

Multibranch pipelines are ideal for:
- Branch-based development workflows
- Automatic pipeline creation for new branches
- Parallel builds across multiple branches
- Environment-specific deployments based on branch names

## Pipeline Features

### Checkout Stage
- **Automatic Branch Detection**: Jenkins automatically checks out the appropriate branch
- **Branch Information**: Displays current branch name in build logs

### Build Stage
- **Node.js Environment**: Uses NodeJS plugin for runtime setup
- **Dependency Management**: Runs `npm install` for dependency resolution
- **Build Execution**: Executes `npm run build` for application compilation

### Test Stages
- **Unit Tests**: Runs `npm test` for comprehensive unit testing
- **Integration Tests**: Optional integration test suite (parameter-controlled)
- **Test Reporting**: Publishes JUnit XML results for visualization

### Deployment Stages
- **Branch-based Logic**: Different deployment environments based on branch
  - `development` → Development environment
  - `staging` → Staging environment
  - `master`/`main` → Production environment
- **Environment-specific Actions**: Customize deployment per environment

### Archive Stage
- **Build Artifacts**: Archives `dist/`, `build/`, or output directories
- **Cross-build Tracking**: Enables artifact tracking across builds

## Configuration Parameters

The pipeline supports:
- **`RUN_INTEGRATION_TESTS`**: Enable/disable integration tests (default: false)

## Prerequisites

### Jenkins Setup
- Multibranch Pipeline plugin installed
- NodeJS plugin installed
- Node.js tool configured in Global Tool Configuration
- Git plugin for repository access

### Project Setup
Your JavaScript project needs a `package.json` with:
```json
{
  "scripts": {
    "build": "your-build-command",
    "test": "your-test-command",
    "integration-test": "your-integration-tests"
  }
}
```

### Branch Structure
Organize your repository with branches for different environments:
- `development` - Development deployments
- `staging` - Staging/testing deployments
- `master`/`main` - Production deployments

## Jenkins Job Setup

1. **Create Multibranch Pipeline**
   - Go to Jenkins → New Item → Multibranch Pipeline
   - Enter project name

2. **Branch Sources**
   - Add source (Git)
   - Enter repository URL
   - Configure credentials
   - Set branch discovery behavior

3. **Build Configuration**
   - Select "by Jenkinsfile"
   - Set script path to `JS/multibranch/Jenkinsfile`
   - Configure scan triggers (periodic or webhook)

4. **Property Strategy**
   - Configure parameters if needed
   - Set up branch property strategies

## Usage

### Automatic Branch Detection
- Push to a new branch → Jenkins automatically creates a pipeline job
- Delete a branch → Jenkins automatically removes the pipeline job
- Branch naming conventions determine deployment targets

### Build Execution
- Builds run automatically on branch changes
- Parallel execution across multiple branches
- Branch-specific deployment logic

### Monitoring
- View all branch jobs in the multibranch project dashboard
- Individual branch job pages show build history
- Test results and artifacts per branch

## Customization

### Additional Branch Deployments
```groovy
stage('Deploy Feature') {
    when { branch 'feature/*' }
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run deploy:feature'
        }
    }
}
```

### Environment-Specific Builds
```groovy
stage('Build') {
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            script {
                if (env.BRANCH_NAME == 'production') {
                    sh 'npm run build:prod'
                } else {
                    sh 'npm run build:dev'
                }
            }
        }
    }
}
```

### Custom Deployment Logic
```groovy
stage('Deploy to Cloud') {
    when { anyOf { branch 'main'; branch 'master' } }
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run deploy:production'
        }
    }
}
```

### Additional Test Stages
```groovy
stage('Performance Tests') {
    when { branch 'staging' }
    steps {
        nodejs(nodeJSInstallationName: 'node') {
            sh 'npm run test:performance'
        }
    }
    post {
        always { junit 'test-results/performance/**/*.xml' }
    }
}
```

## Branch Naming Strategies

### Git Flow
- `master`/`main` - Production releases
- `develop` - Integration branch
- `feature/*` - Feature development
- `release/*` - Release preparation
- `hotfix/*` - Hotfix branches

### Environment-based
- `development` - Development environment
- `staging` - Staging environment
- `production` - Production environment

## Troubleshooting

### Common Issues
- **Branch jobs not created**: Check repository permissions and branch discovery settings
- **Build failures**: Verify Node.js configuration and package.json scripts
- **Deployment not triggered**: Confirm branch naming matches `when` conditions
- **Test results missing**: Check test framework XML output configuration

### Debug Steps
1. Check branch discovery logs in the multibranch job
2. Verify Jenkinsfile path is correct
3. Test pipeline manually on a specific branch
4. Check webhook configuration for automatic triggering

### Performance Considerations
- Large numbers of branches can impact Jenkins performance
- Consider branch pruning strategies
- Use lightweight checkouts for faster builds
- Configure appropriate build timeouts

## Best Practices

### Branch Management
- Use consistent branch naming conventions
- Regularly clean up old feature branches
- Protect important branches (main/master) with required reviews

### Pipeline Optimization
- Use `when` conditions to skip unnecessary stages
- Cache node_modules between builds when possible
- Parallelize independent test suites
- Use appropriate resource allocation per branch

## Related Documentation

- [JS README](../README.md) - JavaScript examples overview
- [Main Multibranch](../../multibranch/) - General multibranch examples
- [Jenkins Multibranch Pipeline](https://jenkins.io/doc/book/pipeline/multibranch/) - Official documentation