# syntax=docker/dockerfile:1

# Naming the stage with the AS command
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build 

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.10_7-jre-alpine-3.21

WORKDIR /app
# we copy --from the previous stage and rename the jar
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]