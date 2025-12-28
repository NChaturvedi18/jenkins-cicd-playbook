# Freestyle Jobs

Freestyle jobs are the traditional way to configure Jenkins builds through the web UI. They are ideal for simple CI/CD workflows where you don't need advanced scripting or branch-based automation.

## Overview

Unlike Pipeline jobs, freestyle jobs are configured entirely in the Jenkins UI, making them accessible for users without Groovy knowledge. This directory provides an example of converting a freestyle job to use a declarative pipeline for better maintainability.

## Example

The `Jenkinsfile` in this directory demonstrates a complete CI/CD pipeline for a Maven-based Java project:
- Checks out code from a specified branch
- Builds the project
- Runs unit and integration tests
- Archives build artifacts

## Setup in Jenkins

1. Create a new Freestyle project in Jenkins.
2. Under Source Code Management, select Git and enter your repository URL and branch.
3. Add build steps:
   - Use "Execute shell" for Maven commands, or
   - Install the Pipeline plugin and add a "Pipeline" build step to execute the `Jenkinsfile`.
4. Configure parameters (e.g., BRANCH, RUN_INTEGRATION_TESTS) under "This project is parameterized".
5. Set up build triggers and post-build actions as needed.

## Usage

- Trigger builds manually or schedule them.
- Monitor results in the Jenkins dashboard.
- Download artifacts from the build page.

For more advanced workflows, consider migrating to Multibranch Pipelines.

See the main [README](../README.md) for additional pipeline types.