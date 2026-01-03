# Tools and Technology Overview

This document explains the tools and technologies used in the Jenkins CI/CD setup for freestyle jobs, multibranch pipelines, and shared libraries.

## Freestyle Jobs
Freestyle jobs in Jenkins are basic, flexible jobs configured via the Jenkins web UI or scripted pipelines. They use:
- **Jenkins**: The core CI/CD platform for job orchestration.
- **Groovy Scripts**: Embedded in the Jenkinsfile for build steps.
- **Git**: For source code checkout.
- **Maven**: For building Java projects (e.g., compiling, packaging).
- **JUnit**: For test result publishing.
- **Shell Scripts**: For executing commands on build agents.

## Multibranch Pipelines
Multibranch pipelines automatically create pipelines for each branch in a Git repository. They use:
- **Jenkins Pipeline**: Declarative or scripted pipelines defined in Jenkinsfile.
- **Git**: Source control management for branching and versioning.
- **Groovy**: Language for Jenkinsfile scripts.
- **Maven**: For dependency management and builds.
- **JUnit**: For test reporting and integration.

## Shared Library
Shared libraries provide reusable code for Jenkins pipelines. They use:
- **Jenkins Shared Libraries**: Framework for storing and loading Groovy code.
- **Groovy**: Primary language for library functions (e.g., vars/buildAndTest.groovy).
- **Maven**: Integrated for build and test stages.
- **JUnit**: For publishing test results.
- **SonarQube**: Optional static code analysis.
- **Pipeline Steps**: Like `withMaven`, `withSonarQubeEnv` for tool integration.