# Stage 1: Build the JAR using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Cache dependencies first
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy all files needed for the build
COPY . .

# Build the application (skip tests to speed up)
RUN mvn clean package -DskipTests

# Stage 2: Run the application with only the JDK
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar JobApp.jar

# Expose the application port
EXPOSE 8080

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "JobApp.jar"]