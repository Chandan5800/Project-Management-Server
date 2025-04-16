# Use a Maven image compatible with OpenJDK
FROM maven:3.11.0-openjdk-17 AS build
WORKDIR /app

# Copy the POM file and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code and package it
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK runtime for the final image
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/projectManagementSystem-0.0.1-SNAPSHOT.jar .

# Expose the application's default port
EXPOSE 8080

# Set the container's entry point
ENTRYPOINT ["java", "-jar", "/app/projectManagementSystem-0.0.1-SNAPSHOT.jar"]
