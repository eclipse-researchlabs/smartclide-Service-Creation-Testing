# Smartclide Service Creation Testing
SmartCLIDE Service Creation Testing Backend Component

## Preconditions to build and run Service Creation Testing

To build and run the backend service of Service Creation Testing, the following software is required:

- Java (at least version 8)
- Apache Maven (at least version 3.2+)
- Docker (for building and running the final image)

## How to build Service Creation Testing

Service Creation Testing can be built using maven with the following command:

```shell
mvn install
```

In order to build a Docker image of the service that can be deployed, the following commands can be used:

```shell
mvn install
docker build -t ${IMAGE_NAME:IMAGE_TAG} .
```

More specifically:

```shell
mvn install
docker build -t smartclide-service-creation-testing-backend:latest .
```

## How to run Service Creation Testing

All the images of this component can be found [here](https://github.com/eclipse-researchlabs/smartclide-Service-Creation-Testing/pkgs/container/smartclide%2Fservice-creation-test-generation).

You can run the backend service with the following command:

```shell
docker run smartclide-service-creation-testing-backend:latest
```

## How to use Service Creation Testing

This Spring Boot application generates automatically Unit tests, for a given project.

The service includes one endpoint:

**Endpoint:** "/generateTests" -> automatic generation of Unit tests.    
**Request parameters:**    
**gitRepoURL** -> String    
**gitUsername** -> String    
**gitToken** -> String 