# Use a multi-stage build to ensure dependencies are resolved in one step and the application runs in another

# Step 1: Build the JAR file
FROM gradle:7.3.3-jdk11 AS build
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon

# Step 2: Use the JAR file in a lightweight image to run the application
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/new-1-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that the Spring Boot application runs on
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

