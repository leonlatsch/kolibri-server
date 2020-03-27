# Start from openjdk-11
FROM openjdk:11-jdk-slim

# Expose 8080 for tomcat webserver
EXPOSE 8080

ARG JAR=target/kolibri-server-*.jar
ARG SH=assets/entrypoint.sh
ARG INST=assets/install.sh

# Add files
ADD ${JAR} kolibri-server-*.jar
ADD ${SH} entrypoint.sh
ADD ${INST} install.sh

# Run install script
RUN sh /install.sh

# Entry point
ENTRYPOINT ["sh", "/entrypoint.sh"]
