# smartclide-Service-Creation-Testing

A Spring service for the automatic generation of Unit tests, for a given project.
The service uses the default Spring port 8080.

The service includes one endpoint:
Endpoint: "/generateTests" -> automatic generation of Unit tests.
Request parameters:
gitRepoURL -> String
gitUsername -> String
gitToken -> String

