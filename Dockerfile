FROM maven:3.8.4-jdk-11

RUN apt-get update && apt-get install -y git

VOLUME /tmp
ADD target/smartclide-Service-Creation-Testing-0.0.1-SNAPSHOT.jar app.jar
ADD src/main/resources/randoop-all-4.3.0.jar randoop-all-4.3.0.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]