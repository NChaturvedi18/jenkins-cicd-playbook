# Multibranch Pipelines

Multibranch Pipelines in Jenkins automatically create and manage pipelines for each branch in a repository. They are ideal for projects with multiple branches, such as feature branches, allowing for parallel builds and branch-specific logic.

## Overview

Unlike Freestyle jobs, Multibranch Pipelines use a `Jenkinsfile` in the repository root or branch to define the pipeline. Jenkins scans the repository for branches and creates jobs accordingly.

## Example

The `Jenkinsfile` in this directory demonstrates a CI/CD pipeline with branch-based deployments:
- Builds and tests for all branches
- Deploys to different environments based on branch name (development, staging, production)

## Setup in Jenkins

1. Create a new Multibranch Pipeline project in Jenkins.
2. Under Branch Sources, add your Git repository.
3. Configure the build configuration to use the `Jenkinsfile` from the repository.
4. Set scan triggers (e.g., periodic or webhook-based).
5. Save and let Jenkins scan for branches.

## Usage

- Push to a new branch to automatically create a pipeline job.
- Branch-specific stages run only on matching branches.
- View branch jobs in the Multibranch project dashboard.

For simpler workflows, consider Freestyle jobs.

See the main [README](../README.md) for additional pipeline types.