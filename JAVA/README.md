# Java/Maven Pipeline Examples

This directory contains Jenkins CI/CD pipeline examples specifically adapted for Java projects using Maven. These examples demonstrate how to build, test, and deploy Java applications using Maven and related tools.

## Directory Structure

- **[freestyle-jobs/](freestyle-jobs/)**: Freestyle job pipeline for Java/Maven projects
  - `Jenkinsfile`: Declarative pipeline for freestyle Jenkins jobs using Maven
- **[multibranch/](multibranch/)**: Multibranch pipeline for Java/Maven projects
  - `Jenkinsfile`: Declarative pipeline for multibranch jobs using Maven
- **[shared-library/](shared-library/)**: Reusable shared library functions for Java/Maven projects
  - `vars/buildAndTest.groovy`: Shared library function for building and testing Java projects

## Prerequisites

- Jenkins server with the following plugins:
  - Pipeline
  - Maven Integration
  - Git
  - JUnit (for test reporting)
- Maven installation configured in Jenkins (Global Tool Configuration)
- JDK installation configured in Jenkins
- A Java project with `pom.xml`

## Key Features

### Build Process
- **Dependency Resolution**: Uses Maven to download and resolve dependencies
- **Compilation**: Compiles Java source code
- **Packaging**: Creates JAR/WAR artifacts
- **Artifact Archiving**: Archives build artifacts for deployment

### Testing
- **Unit Tests**: Executes JUnit/TestNG tests during build
- **Integration Tests**: Supports Maven profiles for integration testing
- **Test Reporting**: Publishes JUnit XML results automatically

### Code Quality
- **SonarQube Integration**: Optional static code analysis
- **Code Coverage**: Integration with JaCoCo or similar tools

## Configuration

### pom.xml Setup
Ensure your Maven project includes appropriate plugins:
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.0.0</version>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-failsafe-plugin</artifactId>
      <version>3.0.0</version>
    </plugin>
  </plugins>
</build>
```

### Jenkins Maven Configuration
1. Go to Jenkins → Manage Jenkins → Global Tool Configuration
2. Add Maven installation with your preferred version
3. Add JDK installation
4. Reference them in pipelines as `maven: 'maven-3.8.6'` and `jdk: 'jdk-11'`

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
                buildAndTest(
                    skipTests: false,
                    runIntegrationTests: true,
                    mavenGoals: 'clean package',
                    jdkVersion: 'jdk-11',
                    mavenVersion: 'maven-3.8.6'
                )
            }
        }
    }
}
```

## Customization

### Maven Goals
Modify the Maven goals to suit your build process:
```groovy
sh "mvn -B -DskipTests clean compile"
```

### Additional Test Types
Add custom test stages as needed:
```groovy
stage('Performance Tests') {
    steps {
        withMaven(maven: 'maven-3.8.6', jdk: 'jdk-11') {
            sh 'mvn -B -Pperformance verify'
        }
    }
}
```

### Deployment Configuration
Customize deployment steps for your infrastructure:
```groovy
stage('Deploy to Tomcat') {
    when { branch 'main' }
    steps {
        deploy adapters: [tomcat9(credentialsId: 'tomcat-creds', url: 'http://tomcat-server:8080')], war: 'target/*.war'
    }
}
```

## Troubleshooting

### Common Issues
- **Maven not found**: Ensure Maven Integration plugin is installed and configured
- **JDK version mismatch**: Verify JDK version compatibility with your project
- **Test failures**: Check test logs and ensure test dependencies are correct
- **Memory issues**: Increase Jenkins executor memory or Maven heap size

### Debug Tips
- Add `sh 'mvn --version && java -version'` to verify installations
- Use `echo` statements to debug pipeline variables
- Check Jenkins build logs for detailed Maven output

## Contributing

When contributing Java pipeline examples:
1. Test with multiple JDK and Maven versions
2. Include sample pom.xml configurations
3. Document any required Jenkins plugins
4. Provide troubleshooting guidance

## Related Documentation

- [Main README](../README.md) - Overview of all pipeline examples
- [JavaScript Examples](../JS/) - JavaScript/Node.js pipeline examples
- [Jenkins Maven Plugin](https://plugins.jenkins.io/maven-plugin/) - Plugin documentation