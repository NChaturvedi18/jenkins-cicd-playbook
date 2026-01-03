# Shared Libraries

Shared Libraries in Jenkins allow you to write reusable pipeline code that can be shared across multiple jobs. This promotes code reuse, standardization, and easier maintenance.

## Overview

Shared libraries are Groovy scripts stored in a Git repository. They can define custom steps, functions, and classes that pipelines can import and use.

## Structure

- `vars/`: Contains global variables and functions (e.g., `buildAndTest.groovy`)
- `src/`: (Optional) For more complex classes
- `resources/`: (Optional) For static resources

## Example

The `vars/buildAndTest.groovy` file defines a reusable function for building and testing Maven projects. It can be called in any pipeline like:

```groovy
@Library('my-shared-lib') _

pipeline {
    agent any
    stages {
        stage('CI') {
            steps {
                buildAndTest(runIntegrationTests: true)
            }
        }
    }
}
```

## Setup in Jenkins

1. Go to Manage Jenkins > Configure System > Global Pipeline Libraries.
2. Add a new library:
   - Name: e.g., `my-shared-lib`
   - Default version: e.g., `main`
   - Retrieval method: Modern SCM
   - Source: Git repository URL
3. In your pipeline, load the library with `@Library('my-shared-lib') _`

## Usage

- Define functions in `vars/` for simple steps.
- Use classes in `src/` for complex logic.
- Version your library for stability.

For pipeline-specific code, consider inline scripts.

See the main [README](../README.md) for additional pipeline types.