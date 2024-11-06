# Use an official OpenJDK image from the Docker Hub
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your local machine to the container
COPY build/libs/new-1-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot application will run on
EXPOSE 9090

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
