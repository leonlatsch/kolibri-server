# Start from openjdk-8
FROM openjdk:8-jdk-alpine

VOLUME /oliviabackend

# Expose 8080 for tomcat webserver
EXPOSE 8080

ARG JAR=target/olivia-backend-1.0-SNAPSHOT.jar

# Add jar file
ADD ${JAR} olivia-backend-1.0-SNAPSHOT.jar

# Entry point
ENTRYPOINT ["java", "-jar", "/olivia-backend-1.0-SNAPSHOT.jar"]

