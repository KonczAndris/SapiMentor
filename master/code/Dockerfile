# Use the maven image to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Build the final application image
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /app/target/registration-login-spring-boot-security-thymeleaf-0.0.1-SNAPSHOT.jar Sapimentor_Demo_Project.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Sapimentor_Demo_Project.jar"]
