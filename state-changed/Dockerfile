# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

# Add Maintainer Info
LABEL maintainer="jakub.pilimon@gmail.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8888 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/state-changed-0.0.1-SNAPSHOT.jar

# Add the application's jar to the container
ADD ${JAR_FILE} state-changed.jar

# Run the jar file 
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/state-changed.jar"]